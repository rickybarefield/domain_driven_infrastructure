package com.appagility.domaindriveninfrastructure.base;

import com.appagility.Builder;

import java.util.List;

public abstract class Tier<TComponent extends Component> {

    protected final String name;

    protected final List<LoadBalancedEndpoint> exposes;

    protected final List<TComponent> components;
    
    protected final NamingStrategy namingStrategy;

    public Tier(NamingStrategy namingStrategy, List<LoadBalancedEndpoint> exposes, List<TComponent> components, String name) {
        this.namingStrategy = namingStrategy;

        this.name = name;
        this.exposes = exposes;
        this.components = components;
    }

    public abstract void defineInfrastructure();

    public interface TierBuilder<TComponent extends Component> extends Builder<Tier<TComponent>> {

        TierBuilder<TComponent> namingStrategy(NamingStrategy resourceNamer);

        TierBuilder<TComponent> name(String name);

        TierBuilder<TComponent> exposes(LoadBalancedEndpoint loadBalancedEndpoint);

        default TierBuilder<TComponent> exposes(Endpoint endpoint, int onPort) {

            return exposes(new LoadBalancedEndpoint(endpoint, onPort));
        }

        TierBuilder<TComponent> component(TComponent component);
    }

    public record LoadBalancedEndpoint(Endpoint target, int port) {

    }
}
