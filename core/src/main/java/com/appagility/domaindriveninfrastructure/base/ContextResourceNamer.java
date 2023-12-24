package com.appagility.domaindriveninfrastructure.base;

import com.pulumi.Context;

public class ContextResourceNamer implements ResourceNamer {

    private final String prefix;

    public ContextResourceNamer(Context pulumiContext) {

        prefix = pulumiContext.projectName() + "-" + pulumiContext.stackName() + "-";
    }

    @Override
    public String generateName(String resourceSpecificContext) {

        return prefix + resourceSpecificContext;
    }
}
