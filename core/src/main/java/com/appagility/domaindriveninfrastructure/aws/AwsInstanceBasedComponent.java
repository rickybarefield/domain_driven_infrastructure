package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.base.Endpoint;
import com.appagility.domaindriveninfrastructure.base.GoldenAmi;
import com.appagility.domaindriveninfrastructure.base.InstanceBasedComponent;
import com.appagility.domaindriveninfrastructure.base.StorageRequirement;
import com.pulumi.aws.autoscaling.Group;
import com.pulumi.aws.autoscaling.GroupArgs;
import com.pulumi.aws.autoscaling.inputs.GroupLaunchTemplateArgs;
import com.pulumi.aws.ec2.LaunchTemplate;
import com.pulumi.aws.ec2.LaunchTemplateArgs;
import com.pulumi.aws.ec2.outputs.GetSubnetsResult;
import com.pulumi.core.Output;
import lombok.Builder;
import lombok.Singular;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public class AwsInstanceBasedComponent extends InstanceBasedComponent<AwsScalingApproach> implements AwsComponent {

    private Optional<Group> group = Optional.empty();

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
    public void defineInfrastructure(Output<GetSubnetsResult> subnets) {

        var subnetIds = subnets.applyValue(GetSubnetsResult::ids);

        var launchTemplate = new LaunchTemplate("mongo-launch", LaunchTemplateArgs.builder()
                .namePrefix("mongo")
                .imageId("ami-1a2b3c")
                .instanceType("t2.micro")
                .build());

        GroupArgs.Builder groupArgsBuilder = GroupArgs.builder()
                .launchTemplate(GroupLaunchTemplateArgs.builder().id(launchTemplate.id()).version("$Latest").build())
                .vpcZoneIdentifiers(subnetIds);

        scalingApproach.addRequirements(groupArgsBuilder);

        group = Optional.of(new Group(shortCode, groupArgsBuilder.build()));
    }

    public Output<String> getTargetGroupArn() {

        return group.map(g -> g.arn()).orElseThrow(() -> new RuntimeException("Invalid state, group not created"));
    }

    public static class AwsInstanceBasedComponentBuilder implements InstanceBasedComponentBuilder<AwsScalingApproach> {

    }
}
