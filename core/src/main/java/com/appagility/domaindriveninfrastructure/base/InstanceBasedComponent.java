package com.appagility.domaindriveninfrastructure.base;

import com.appagility.Builder;
import lombok.Getter;

import java.util.List;

public abstract class InstanceBasedComponent<
        TInstanceBasedComponent extends InstanceBasedComponent<TInstanceBasedComponent, TEndpoint, TScalingApproach>,
        TEndpoint extends InternalEndpoint<TInstanceBasedComponent>,
        TScalingApproach extends ScalingApproach> implements Component {

    @Getter
    protected final NamingStrategy namingStrategy;

    protected final String shortCode;

    protected final GoldenAmi basedOn;

    protected final TScalingApproach scalingApproach;

    protected final List<TEndpoint> endpointsExposed;

    protected final List<TEndpoint> endpointsAccessed;

    protected final StorageRequirement storageRequirements;


    public InstanceBasedComponent(NamingStrategy namingStrategy,
                                  String shortCode,
                                  GoldenAmi basedOn,
                                  TScalingApproach scalingApproach,
                                  List<TEndpoint> endpointsExposed,
                                  List<TEndpoint> endpointsAccessed,
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

    public interface InstanceBasedComponentBuilder<
            TInstanceBasedComponent extends InstanceBasedComponent<TInstanceBasedComponent, TEndpoint, TScalingApproach>,
            TEndpoint extends InternalEndpoint<TInstanceBasedComponent>,
            TScalingApproach extends ScalingApproach> extends Builder<InstanceBasedComponent<TInstanceBasedComponent, TEndpoint, TScalingApproach>> {

        InstanceBasedComponentBuilder<TInstanceBasedComponent, TEndpoint, TScalingApproach> namingStrategy(NamingStrategy resourceNamer);

        InstanceBasedComponentBuilder<TInstanceBasedComponent,TEndpoint, TScalingApproach> shortCode(String shortCode);

        InstanceBasedComponentBuilder<TInstanceBasedComponent,TEndpoint, TScalingApproach> basedOn(GoldenAmi base);

        InstanceBasedComponentBuilder<TInstanceBasedComponent,TEndpoint, TScalingApproach> scalingApproach(TScalingApproach scalingApproach);

        InstanceBasedComponentBuilder<TInstanceBasedComponent,TEndpoint, TScalingApproach> exposes(TEndpoint endpoint);

        InstanceBasedComponentBuilder<TInstanceBasedComponent,TEndpoint, TScalingApproach> accesses(TEndpoint endpoint);
    }
}
