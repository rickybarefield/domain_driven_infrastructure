package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.base.ScalingApproach;
import com.pulumi.aws.autoscaling.GroupArgs;
import lombok.Builder;

public class AwsScalingApproach extends ScalingApproach {

    @Builder
    public AwsScalingApproach(int minInstances, int maxInstances) {

        super(minInstances, maxInstances);
    }

    public void addRequirements(GroupArgs.Builder groupArgsBuilder) {

        groupArgsBuilder.minSize(minInstances).maxSize(maxInstances);
    }

    public static class AwsScalingApproachBuilder implements ScalingApproachBuilder {

    }
}
