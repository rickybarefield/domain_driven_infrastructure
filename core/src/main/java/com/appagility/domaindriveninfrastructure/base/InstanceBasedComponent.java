package com.appagility.domaindriveninfrastructure.base;

import com.appagility.Builder;

import java.util.List;

public abstract class InstanceBasedComponent<TScalingApproach extends ScalingApproach> implements Component {

    protected final String shortCode;

    protected final GoldenAmi basedOn;

    protected final TScalingApproach scalingApproach;

    protected final List<Endpoint> endpointsExposed;

    protected final List<Endpoint> endpointsAccessed;

    protected final StorageRequirement storageRequirements;


    public InstanceBasedComponent(String shortCode, GoldenAmi basedOn, TScalingApproach scalingApproach, List<Endpoint> endpointsExposed, List<Endpoint> endpointsAccessed, StorageRequirement storageRequirements) {

        this.shortCode = shortCode;
        this.basedOn = basedOn;
        this.scalingApproach = scalingApproach;
        this.endpointsExposed = endpointsExposed;
        this.endpointsAccessed = endpointsAccessed;
        this.storageRequirements = storageRequirements;
    }

    public interface InstanceBasedComponentBuilder<TScalingApproach
            extends ScalingApproach> extends Builder<InstanceBasedComponent<TScalingApproach>> {

        InstanceBasedComponentBuilder shortCode(String shortCode);

        InstanceBasedComponentBuilder basedOn(GoldenAmi base);

        InstanceBasedComponentBuilder scalingApproach(TScalingApproach scalingApproach);

        InstanceBasedComponentBuilder exposes(Endpoint service);
    }

    public boolean doesExpose(Endpoint endpoint) {

        return endpointsExposed.contains(endpoint);
    }
}
