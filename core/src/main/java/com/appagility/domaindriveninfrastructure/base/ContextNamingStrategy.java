package com.appagility.domaindriveninfrastructure.base;

import com.pulumi.Context;

public class ContextNamingStrategy implements NamingStrategy {

    private final String prefix;

    public ContextNamingStrategy(Context pulumiContext) {

        prefix = pulumiContext.projectName() + "-" + pulumiContext.stackName() + "-";
    }

    @Override
    public String generateName(String resourceSpecificContext) {

        return prefix + resourceSpecificContext;
    }
}
