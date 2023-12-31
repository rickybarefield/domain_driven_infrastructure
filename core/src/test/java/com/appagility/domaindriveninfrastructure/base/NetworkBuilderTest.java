package com.appagility.domaindriveninfrastructure.base;

import com.appagility.domaindriveninfrastructure.aws.AwsNetwork;
import inet.ipaddr.IPAddressString;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class NetworkBuilderTest {


    @Test
    public void testHalvingARange() {

        var network = AwsNetwork.builder()
                .withEvenlySplitSubnets(new IPAddressString("10.1.0.0/16").getAddress(), 2).build();

        var subnets = network.getSubnets();

        assertThat(subnets, hasSize(2));

        var firstSubnet = subnets.get(0);
        var secondSubnet = subnets.get(1);

        assertThat(firstSubnet.getRange().toString(), equalTo("10.1.0.0/17"));
        assertThat(secondSubnet.getRange().toString(), equalTo("10.1.128.0/17"));

        assertThat(firstSubnet.getRange().getCount(), equalTo(new BigInteger("32768")));
        assertThat(secondSubnet.getRange().getCount(), equalTo(new BigInteger("32768")));
    }

    @Test
    public void testQuarteringARange() {

        var network = AwsNetwork.builder()
                .withEvenlySplitSubnets(new IPAddressString("17.2.1.0/24").getAddress(), 4).build();

        var subnets = network.getSubnets();

        assertThat(subnets, hasSize(4));

        var firstSubnet = subnets.get(0);
        var secondSubnet = subnets.get(1);
        var thirdSubnet = subnets.get(2);
        var fourthSubnet = subnets.get(3);

        assertThat(firstSubnet.getRange().toString(), equalTo("17.2.1.0/26"));
        assertThat(secondSubnet.getRange().toString(), equalTo("17.2.1.64/26"));
        assertThat(thirdSubnet.getRange().toString(), equalTo("17.2.1.128/26"));
        assertThat(fourthSubnet.getRange().toString(), equalTo("17.2.1.192/26"));

        subnets.forEach(s -> assertThat(s.getRange().getCount(), equalTo(new BigInteger("64"))));
    }

    @Test
    public void testWhenNumberOfSubnetsIsNotPowerOfTwo() {

        var network = AwsNetwork.builder()
                .withEvenlySplitSubnets(new IPAddressString("10.0.1.0/24").getAddress(), 5).build();

        var subnets = network.getSubnets();

        assertThat(subnets, hasSize(5));

        //Five subnets will round up to 8 as 2^3=8, so prefix length of 3 will be used
        var firstSubnet = subnets.get(0);
        var secondSubnet = subnets.get(1);
        var thirdSubnet = subnets.get(2);
        var fourthSubnet = subnets.get(3);
        var fithSubnet = subnets.get(4);

        assertThat(firstSubnet.getRange().toString(), equalTo("10.0.1.0/27"));
        assertThat(secondSubnet.getRange().toString(), equalTo("10.0.1.32/27"));
        assertThat(thirdSubnet.getRange().toString(), equalTo("10.0.1.64/27"));
        assertThat(fourthSubnet.getRange().toString(), equalTo("10.0.1.96/27"));
        assertThat(fithSubnet.getRange().toString(), equalTo("10.0.1.128/27"));

        subnets.forEach(s -> assertThat(s.getRange().getCount(), equalTo(new BigInteger("32"))));
    }
}
