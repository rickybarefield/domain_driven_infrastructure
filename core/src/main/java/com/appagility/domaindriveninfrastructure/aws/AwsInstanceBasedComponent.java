package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.MayBecome;
import com.appagility.domaindriveninfrastructure.base.Endpoint;
import com.appagility.domaindriveninfrastructure.base.GoldenAmi;
import com.appagility.domaindriveninfrastructure.base.InstanceBasedComponent;
import com.appagility.domaindriveninfrastructure.base.StorageRequirement;
import com.pulumi.aws.autoscaling.Group;
import com.pulumi.aws.autoscaling.GroupArgs;
import com.pulumi.aws.autoscaling.inputs.GroupLaunchTemplateArgs;
import com.pulumi.aws.ec2.LaunchTemplate;
import com.pulumi.aws.ec2.LaunchTemplateArgs;
import com.pulumi.aws.ec2.SecurityGroup;
import com.pulumi.aws.ec2.outputs.GetSubnetsResult;
import com.pulumi.core.Output;
import lombok.Builder;
import lombok.Singular;

import java.util.List;

public class AwsInstanceBasedComponent extends InstanceBasedComponent<AwsScalingApproach> implements AwsComponent, AwsSecurable {

    private MayBecome<Group> group = MayBecome.empty("group");

    private MayBecome<SecurityGroup> securityGroup = MayBecome.empty("securityGroup");

    @Builder
    public AwsInstanceBasedComponent(String shortCode,
                                     GoldenAmi basedOn,
                                     AwsScalingApproach scalingApproach,
                                     @Singular("exposes") List<Endpoint> servicesExposed,
                                     List<Endpoint> servicesAccessed,
                                     StorageRequirement storageRequirements) {

        super(shortCode, basedOn, scalingApproach, servicesExposed, servicesAccessed, storageRequirements);
    }

    @Override
    public String getName() {

        return shortCode;
    }

    @Override
    public void defineInfrastructure(Output<GetSubnetsResult> subnets) {

        var subnetIds = subnets.applyValue(GetSubnetsResult::ids);

        securityGroup.set(new SecurityGroup("mongo"));

        var launchTemplate = new LaunchTemplate("mongo-launch", LaunchTemplateArgs.builder()
                .namePrefix("mongo")
                .imageId("ami-1a2b3c")
                .instanceType("t2.micro")
                .vpcSecurityGroupIds(securityGroup.get().id().applyValue(List::of))
                .build());

        GroupArgs.Builder groupArgsBuilder = GroupArgs.builder()
                .launchTemplate(GroupLaunchTemplateArgs.builder().id(launchTemplate.id()).version("$Latest").build())
                .vpcZoneIdentifiers(subnetIds);

        scalingApproach.addRequirements(groupArgsBuilder);

        group.set(new Group(shortCode, groupArgsBuilder.build()));
    }

    public Output<String> getTargetGroupArn() {

        return group.get().arn();
    }

    @Override
    public SecurityGroup getSecurityGroup() {

        return securityGroup.get();
    }

    public static class AwsInstanceBasedComponentBuilder implements InstanceBasedComponentBuilder<AwsScalingApproach> {

    }
}
