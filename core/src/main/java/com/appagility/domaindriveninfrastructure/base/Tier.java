package com.appagility.domaindriveninfrastructure.base;

import com.appagility.Builder;

import java.util.List;

public abstract class Tier<TComponent extends Component> {

    protected final String name;

    private final List<Endpoint> exposes;

    protected final List<TComponent> components;

    public Tier(String name, List<Endpoint> exposes, List<TComponent> components) {

        this.name = name;
        this.exposes = exposes;
        this.components = components;
    }

    public abstract void defineInfrastructure();

    public interface TierBuilder<TComponent extends Component> extends Builder<Tier<TComponent>> {

        TierBuilder<TComponent> name(String name);

        TierBuilder<TComponent> exposes(Endpoint service);

        TierBuilder<TComponent> component(TComponent component);
    }
}
