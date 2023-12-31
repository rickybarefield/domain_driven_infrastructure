package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.Subnet;
import inet.ipaddr.IPAddress;

public class AwsSubnet extends Subnet {

    public AwsSubnet(IPAddress range) {
        super(range);
    }
}
