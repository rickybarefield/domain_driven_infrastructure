package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.MayBecome;
import com.appagility.domaindriveninfrastructure.base.GoldenAmi;
import com.appagility.domaindriveninfrastructure.base.InstanceBasedComponent;
import com.appagility.domaindriveninfrastructure.base.NamingStrategy;
import com.appagility.domaindriveninfrastructure.base.StorageRequirement;
import com.google.common.base.Suppliers;
import com.pulumi.aws.autoscaling.Group;
import com.pulumi.aws.autoscaling.GroupArgs;
import com.pulumi.aws.autoscaling.inputs.GroupLaunchTemplateArgs;
import com.pulumi.aws.ec2.LaunchTemplate;
import com.pulumi.aws.ec2.LaunchTemplateArgs;
import com.pulumi.aws.ec2.SecurityGroup;
import com.pulumi.aws.ec2.outputs.GetSubnetsResult;
import com.pulumi.core.Output;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;

import java.util.List;
import java.util.function.Supplier;

public class AwsInstanceBasedComponent extends InstanceBasedComponent<
        AwsInstanceBasedComponent,
        AwsEndpoint,
        InternalAwsEndpoint,
        AwsScalingApproach>
        implements AwsComponent, AwsSecurable {

    private final MayBecome<Group> group = MayBecome.empty("group");

    private final Supplier<SecurityGroup> securityGroup = Suppliers.memoize(() ->
            new SecurityGroup(namingStrategy.generateName(shortCode)));

    @Builder
    public AwsInstanceBasedComponent(@NonNull
                                     NamingStrategy namingStrategy,
                                     @NonNull
                                     String shortCode,
                                     @NonNull
                                     GoldenAmi basedOn,
                                     @NonNull
                                     AwsScalingApproach scalingApproach,
                                     @Singular("exposes") List<InternalAwsEndpoint> endpointsExposed,
                                     @Singular("accesses") List<AwsEndpoint> endpointsAccessed,
                                     StorageRequirement storageRequirements) {

        super(namingStrategy, shortCode, basedOn, scalingApproach, endpointsExposed, endpointsAccessed, storageRequirements);
    }

    @Override
    public String getName() {

        return shortCode;
    }

    @Override
    public void defineInfrastructure(Output<GetSubnetsResult> subnets) {

        var subnetIds = subnets.applyValue(GetSubnetsResult::ids);

        var launchTemplate = new LaunchTemplate(namingStrategy.generateName(shortCode),
                LaunchTemplateArgs.builder()
                .namePrefix(shortCode)
                .imageId("ami-1a2b3c")
                .instanceType("t2.micro")
                .vpcSecurityGroupIds(securityGroup.get().id().applyValue(List::of))
                .build());

        GroupArgs.Builder groupArgsBuilder = GroupArgs.builder()
                .launchTemplate(GroupLaunchTemplateArgs.builder().id(launchTemplate.id()).version("$Latest").build())
                .vpcZoneIdentifiers(subnetIds);

        scalingApproach.addRequirements(groupArgsBuilder);

        group.set(new Group(namingStrategy.generateName(shortCode), groupArgsBuilder.build()));

        endpointsAccessed.forEach(this::allowTcpAccessTo);
    }

    public Output<String> getTargetGroupArn() {

        return group.get().arn();
    }

    @Override
    public SecurityGroup getSecurityGroup() {

        return securityGroup.get();
    }

    public static class AwsInstanceBasedComponentBuilder implements InstanceBasedComponentBuilder<AwsInstanceBasedComponent, AwsEndpoint, InternalAwsEndpoint, AwsScalingApproach> {

    }
}
