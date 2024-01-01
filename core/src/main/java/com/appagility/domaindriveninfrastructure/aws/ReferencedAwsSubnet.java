package com.appagility.domaindriveninfrastructure.aws;

import com.pulumi.core.Output;
import lombok.Getter;

public class ReferencedAwsSubnet {

    @Getter
    private final Output<String> id;

    public ReferencedAwsSubnet(Output<String> id) {

        this.id = id;
    }

    public ReferencedAwsSubnet(AwsSubnet awsSubnet) {

        this(awsSubnet.getId());
    }
}
