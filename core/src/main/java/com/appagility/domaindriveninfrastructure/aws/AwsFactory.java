package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.base.Endpoint;
import com.appagility.domaindriveninfrastructure.CloudProviderFactory;
import com.appagility.domaindriveninfrastructure.base.ResourceNamer;

public class AwsFactory implements CloudProviderFactory<
        AwsTier.AwsTierBuilder,
        AwsInstanceBasedComponent.AwsInstanceBasedComponentBuilder,
        AwsScalingApproach,
        AwsScalingApproach.AwsScalingApproachBuilder,
        AwsComponent> {


    private final ResourceNamer resourceNamer;

    public AwsFactory(ResourceNamer resourceNamer) {

        this.resourceNamer = resourceNamer;
    }

    @Override
    public AwsTier.AwsTierBuilder tierBuilder() {

        return AwsTier.builder().resourceNamer(resourceNamer);
    }

    @Override
    public AwsInstanceBasedComponent.AwsInstanceBasedComponentBuilder componentBuilder() {

        return new AwsInstanceBasedComponent.AwsInstanceBasedComponentBuilder().resourceNamer(resourceNamer);
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
