package com.appagility.domaindriveninfrastructure.base;

public interface NamingStrategy {

    String generateName(String resourceSpecificContext);
}
