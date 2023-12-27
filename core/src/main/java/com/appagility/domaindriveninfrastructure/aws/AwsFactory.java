package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.base.Endpoint;
import com.appagility.domaindriveninfrastructure.CloudProviderFactory;
import com.appagility.domaindriveninfrastructure.base.NamingStrategy;

public class AwsFactory implements CloudProviderFactory<
        AwsTier.AwsTierBuilder,
        AwsInstanceBasedComponent.AwsInstanceBasedComponentBuilder,
        AwsScalingApproach,
        AwsScalingApproach.AwsScalingApproachBuilder,
        AwsComponent> {


    private final NamingStrategy namingStrategy;

    public AwsFactory(NamingStrategy namingStrategy) {

        this.namingStrategy = namingStrategy;
    }

    @Override
    public AwsTier.AwsTierBuilder tierBuilder() {

        return AwsTier.builder().namingStrategy(namingStrategy);
    }

    @Override
    public AwsInstanceBasedComponent.AwsInstanceBasedComponentBuilder componentBuilder() {

        return new AwsInstanceBasedComponent.AwsInstanceBasedComponentBuilder().namingStrategy(namingStrategy);
    }

    @Override
    public Endpoint.EndpointBuilder endpointBuilder() {

        return new AwsEndpoint.AwsEndpointBuilder();
    }

    @Override
    public AwsScalingApproach.AwsScalingApproachBuilder scalingApproachBuilder() {

        return new AwsScalingApproach.AwsScalingApproachBuilder();
    }
}
