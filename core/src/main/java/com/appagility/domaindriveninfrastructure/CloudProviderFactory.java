package com.appagility.domaindriveninfrastructure;

import com.appagility.domaindriveninfrastructure.base.*;

public interface CloudProviderFactory<TTierBuilder extends Tier.TierBuilder<TComponent>,
        TComponentBuilder extends InstanceBasedComponent.InstanceBasedComponentBuilder<TScalingApproach>,
        TScalingApproach extends ScalingApproach,
        TScalingApproachBuilder extends ScalingApproach.ScalingApproachBuilder,
        TComponent extends Component> {

    TTierBuilder tierBuilder();

    TComponentBuilder componentBuilder();

    Endpoint.EndpointBuilder endpointBuilder();

    TScalingApproachBuilder scalingApproachBuilder();

}
