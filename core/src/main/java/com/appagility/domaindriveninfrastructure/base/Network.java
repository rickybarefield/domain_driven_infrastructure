package com.appagility.domaindriveninfrastructure.base;

import com.appagility.Builder;
import com.appagility.domaindriveninfrastructure.Subnet;
import inet.ipaddr.IPAddress;
import lombok.Getter;

import java.util.List;

public class Network<TSubnet extends Subnet> {

    @Getter
    private final List<TSubnet> subnets;

    public Network(List<TSubnet> subnets) {

        this.subnets = subnets;
    }


    /**
     * Builds a network with evenly sized subnets
     */
    public interface NetworkBuilder<TSubnet extends Subnet> extends Builder<Network<TSubnet>> {

        NetworkBuilder<TSubnet> subnets(List<TSubnet> subnets);

        default NetworkBuilder<TSubnet> withEvenlySplitSubnets(IPAddress range, int numberOfSubnets) {

            var prefixIncrement = (int)Math.ceil(log2(numberOfSubnets));

            var adjusted = range.setPrefixLength(range.getPrefixLength() + prefixIncrement, false);

            var subnets = adjusted.prefixBlockStream().map(this::createSubnet).limit(numberOfSubnets).toList();

            return subnets(subnets);
        }

        TSubnet createSubnet(IPAddress range);

        private static double log2(int n)
        {
            return  Math.log(n) / Math.log(2);
        }
    }
}
