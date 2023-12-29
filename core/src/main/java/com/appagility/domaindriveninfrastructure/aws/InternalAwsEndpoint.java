package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.base.InternalEndpoint;
import com.appagility.domaindriveninfrastructure.base.Protocol;
import com.pulumi.core.Output;
import lombok.Builder;

public class InternalAwsEndpoint extends InternalEndpoint<AwsInstanceBasedComponent> implements AwsEndpoint {

    @Builder
    public InternalAwsEndpoint(Protocol protocol, int port) {
        super(protocol, port);
    }

    @Override
    public String getName() {

        return getComponent().getName();
    }

    @Override
    public Output<String> getSecurityGroupId() {

        return getComponent().getSecurityGroup().id();
    }

    public static class InternalAwsEndpointBuilder implements EndpointBuilder<AwsInstanceBasedComponent> {
    }
}
