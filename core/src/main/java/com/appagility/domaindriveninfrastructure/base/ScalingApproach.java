package com.appagility.domaindriveninfrastructure.base;

import com.appagility.Builder;

public abstract class ScalingApproach {

    protected int minInstances;

    protected int maxInstances;

    public ScalingApproach(int minInstances, int maxInstances) {

        this.minInstances = minInstances;
        this.maxInstances = maxInstances;
    }

    public interface ScalingApproachBuilder extends Builder<ScalingApproach> {

        ScalingApproachBuilder minInstances(int minInstances);

        ScalingApproachBuilder maxInstances(int maxInstances);
    }
}
