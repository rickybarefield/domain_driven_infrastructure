package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.base.Network;
import inet.ipaddr.IPAddress;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

public class AwsNetwork extends Network<AwsSubnet> {

    @Builder
    public AwsNetwork(List<AwsSubnet> subnets) {

        super(subnets);
    }

    public static class AwsNetworkBuilder implements NetworkBuilder<AwsSubnet> {

        @Override
        public AwsSubnet createSubnet(IPAddress range) {

            return new AwsSubnet(range);
        }
    }

}
