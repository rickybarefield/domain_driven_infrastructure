package com.appagility.domaindriveninfrastructure;

import inet.ipaddr.IPAddress;
import lombok.Getter;

public class Subnet {

    @Getter
    private final IPAddress range;

    public Subnet(IPAddress range) {

        this.range = range;
    }
}
