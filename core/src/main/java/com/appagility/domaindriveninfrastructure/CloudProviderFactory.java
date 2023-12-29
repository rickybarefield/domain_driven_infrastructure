package com.appagility.domaindriveninfrastructure;

import com.appagility.domaindriveninfrastructure.base.*;

public interface CloudProviderFactory<
        TTierBuilder extends Tier.TierBuilder<TComponent, TEndpoint>,
        TScalingApproach extends ScalingApproach,
        TComponentBuilder extends InstanceBasedComponent.InstanceBasedComponentBuilder<TComponent, TEndpoint, TScalingApproach>,
        TEndpoint extends InternalEndpoint<TComponent>,
        TEndpointBuilder extends InternalEndpoint.EndpointBuilder<TComponent>,
        TScalingApproachBuilder extends ScalingApproach.ScalingApproachBuilder,
        TComponent extends InstanceBasedComponent<TComponent, TEndpoint, TScalingApproach>> {

    TTierBuilder tierBuilder();

    TComponentBuilder componentBuilder();

    TEndpointBuilder endpointBuilder();

    TScalingApproachBuilder scalingApproachBuilder();

}
