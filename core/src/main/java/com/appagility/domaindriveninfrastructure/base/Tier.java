package com.appagility.domaindriveninfrastructure.base;

import com.appagility.Builder;

import java.util.List;

public abstract class Tier<TComponent extends Component, TEndpoint extends InternalEndpoint<TComponent>> {

    protected final String name;

    protected final List<LoadBalancedEndpoint<TComponent, TEndpoint>> exposes;

    protected final List<TComponent> components;
    
    protected final NamingStrategy namingStrategy;

    public Tier(NamingStrategy namingStrategy,
                List<LoadBalancedEndpoint<TComponent, TEndpoint>> exposes,
                List<TComponent> components,
                String name) {

        this.namingStrategy = namingStrategy;
        this.name = name;
        this.exposes = exposes;
        this.components = components;
    }

    public abstract void defineInfrastructure();

    public interface TierBuilder<TComponent extends Component, TEndpoint extends InternalEndpoint<TComponent>>
            extends Builder<Tier<TComponent, TEndpoint>> {

        TierBuilder<TComponent, TEndpoint> namingStrategy(NamingStrategy resourceNamer);

        TierBuilder<TComponent, TEndpoint> name(String name);

        TierBuilder<TComponent, TEndpoint> exposes(LoadBalancedEndpoint<TComponent, TEndpoint> loadBalancedEndpoint);

        default TierBuilder<TComponent, TEndpoint> exposes(TEndpoint endpoint, int onPort) {

            return exposes(new LoadBalancedEndpoint<TComponent, TEndpoint>(endpoint, onPort));
        }

        TierBuilder<TComponent, TEndpoint> component(TComponent component);
    }

    public record LoadBalancedEndpoint<
            TComponent extends Component,
            TEndpoint extends InternalEndpoint<TComponent>>(TEndpoint target, int port) {

    }
}
