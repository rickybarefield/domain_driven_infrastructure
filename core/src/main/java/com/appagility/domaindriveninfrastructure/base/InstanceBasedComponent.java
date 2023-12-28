package com.appagility.domaindriveninfrastructure.base;

import com.appagility.Builder;
import lombok.Getter;

import java.util.List;

public abstract class InstanceBasedComponent<TInstanceBasedComponent extends InstanceBasedComponent<TInstanceBasedComponent, TScalingApproach>,
        TScalingApproach extends ScalingApproach> implements Component {

    @Getter
    protected final NamingStrategy namingStrategy;

    protected final String shortCode;

    protected final GoldenAmi basedOn;

    protected final TScalingApproach scalingApproach;

    protected final List<Endpoint<TInstanceBasedComponent>> endpointsExposed;

    protected final List<Endpoint<TInstanceBasedComponent>> endpointsAccessed;

    protected final StorageRequirement storageRequirements;


    public InstanceBasedComponent(NamingStrategy namingStrategy,
                                  String shortCode,
                                  GoldenAmi basedOn,
                                  TScalingApproach scalingApproach,
                                  List<Endpoint<TInstanceBasedComponent>> endpointsExposed,
                                  List<Endpoint<TInstanceBasedComponent>> endpointsAccessed,
                                  StorageRequirement storageRequirements) {


        endpointsExposed.forEach(e -> e.setComponent((TInstanceBasedComponent) this));

        this.namingStrategy = namingStrategy;
        this.shortCode = shortCode;
        this.basedOn = basedOn;
        this.scalingApproach = scalingApproach;
        this.endpointsExposed = endpointsExposed;
        this.endpointsAccessed = endpointsAccessed;
        this.storageRequirements = storageRequirements;
    }

    public interface InstanceBasedComponentBuilder<TInstanceBasedComponent extends InstanceBasedComponent<TInstanceBasedComponent, TScalingApproach>,
            TScalingApproach extends ScalingApproach> extends Builder<InstanceBasedComponent<TInstanceBasedComponent, TScalingApproach>> {

        InstanceBasedComponentBuilder<TInstanceBasedComponent, TScalingApproach> namingStrategy(NamingStrategy resourceNamer);

        InstanceBasedComponentBuilder<TInstanceBasedComponent,TScalingApproach> shortCode(String shortCode);

        InstanceBasedComponentBuilder<TInstanceBasedComponent,TScalingApproach> basedOn(GoldenAmi base);

        InstanceBasedComponentBuilder<TInstanceBasedComponent,TScalingApproach> scalingApproach(TScalingApproach scalingApproach);

        InstanceBasedComponentBuilder<TInstanceBasedComponent,TScalingApproach> exposes(Endpoint<TInstanceBasedComponent> endpoint);

        InstanceBasedComponentBuilder<TInstanceBasedComponent,TScalingApproach> accesses(Endpoint<TInstanceBasedComponent> endpoint);
    }
}
