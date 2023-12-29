package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.base.InternalEndpoint;
import com.appagility.domaindriveninfrastructure.base.Protocol;
import lombok.Builder;

public class AwsEndpoint extends InternalEndpoint<AwsInstanceBasedComponent> {

    @Builder
    public AwsEndpoint(Protocol protocol, int port) {
        super(protocol, port);
    }

    public static class AwsEndpointBuilder implements EndpointBuilder<AwsInstanceBasedComponent> {

    }
}
