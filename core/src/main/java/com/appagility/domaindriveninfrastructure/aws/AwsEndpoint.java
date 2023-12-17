package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.base.Endpoint;
import lombok.Builder;

public class AwsEndpoint extends Endpoint {

    @Builder
    public AwsEndpoint(String protocol, int port) {
        super(protocol, port);
    }

    public static class AwsEndpointBuilder implements EndpointBuilder {

    }
}