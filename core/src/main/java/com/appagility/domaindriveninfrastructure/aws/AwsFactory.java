package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.base.Endpoint;
import com.appagility.domaindriveninfrastructure.CloudProviderFactory;

public class AwsFactory implements CloudProviderFactory<
        AwsTier.AwsTierBuilder,
        AwsInstanceBasedComponent.AwsInstanceBasedComponentBuilder,
        AwsScalingApproach,
        AwsScalingApproach.AwsScalingApproachBuilder,
        AwsComponent> {

    @Override
    public AwsTier.AwsTierBuilder tierBuilder() {

        return AwsTier.builder();
    }

    @Override
    public AwsInstanceBasedComponent.AwsInstanceBasedComponentBuilder componentBuilder() {

        return new AwsInstanceBasedComponent.AwsInstanceBasedComponentBuilder();
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
