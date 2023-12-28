package com.appagility.domaindriveninfrastructure;

import com.appagility.domaindriveninfrastructure.base.*;

public interface CloudProviderFactory<TTierBuilder extends Tier.TierBuilder<TComponent>,
        TScalingApproach extends ScalingApproach,
        TComponentBuilder extends InstanceBasedComponent.InstanceBasedComponentBuilder<TComponent, TScalingApproach>,
        TScalingApproachBuilder extends ScalingApproach.ScalingApproachBuilder,
        TComponent extends InstanceBasedComponent<TComponent, TScalingApproach>> {

    TTierBuilder tierBuilder();

    TComponentBuilder componentBuilder();

    Endpoint.EndpointBuilder<TComponent> endpointBuilder();

    TScalingApproachBuilder scalingApproachBuilder();

}
