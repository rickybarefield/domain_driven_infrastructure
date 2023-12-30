package com.appagility.domaindriveninfrastructure.base;

import com.pulumi.core.Output;

public interface NamingStrategy {

    String generateName(String resourceSpecificContext);
}
