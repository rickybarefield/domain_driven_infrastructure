package com.appagility.domaindriveninfrastructure.aws;

import com.pulumi.core.Output;
import lombok.Getter;

public class ReferencedAwsEndpoint implements AwsEndpoint {

    @Getter
    private final int port;

    @Getter
    private final String name;

    @Getter
    private final Output<String> securityGroupId;

    public ReferencedAwsEndpoint(int port, String name, Output<String> securityGroupId) {

        this.port = port;
        this.name = name;
        this.securityGroupId = securityGroupId;
    }
}
