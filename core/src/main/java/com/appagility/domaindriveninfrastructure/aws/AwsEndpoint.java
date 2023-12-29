package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.base.Endpoint;
import com.pulumi.core.Output;

public interface AwsEndpoint extends Endpoint {

    int getPort();

    String getName();

    Output<String> getSecurityGroupId();
}
