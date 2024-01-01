package com.appagility.domaindriveninfrastructure.base;

import inet.ipaddr.IPAddressString;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class SubnettingTest {


    @Test
    public void testHalvingARange() {

        var subnets = Subnetting.split(new IPAddressString("10.1.0.0/16").getAddress(), 2);

        assertThat(subnets, hasSize(2));

        var firstSubnet = subnets.get(0);
        var secondSubnet = subnets.get(1);

        assertThat(firstSubnet.toString(), equalTo("10.1.0.0/17"));
        assertThat(secondSubnet.toString(), equalTo("10.1.128.0/17"));

        assertThat(firstSubnet.getCount(), equalTo(new BigInteger("32768")));
        assertThat(secondSubnet.getCount(), equalTo(new BigInteger("32768")));
    }

    @Test
    public void testQuarteringARange() {

        var subnets = Subnetting.split(new IPAddressString("17.2.1.0/24").getAddress(), 4);

        assertThat(subnets, hasSize(4));

        var firstSubnet = subnets.get(0);
        var secondSubnet = subnets.get(1);
        var thirdSubnet = subnets.get(2);
        var fourthSubnet = subnets.get(3);

        assertThat(firstSubnet.toString(), equalTo("17.2.1.0/26"));
        assertThat(secondSubnet.toString(), equalTo("17.2.1.64/26"));
        assertThat(thirdSubnet.toString(), equalTo("17.2.1.128/26"));
        assertThat(fourthSubnet.toString(), equalTo("17.2.1.192/26"));

        subnets.forEach(s -> assertThat(s.getCount(), equalTo(new BigInteger("64"))));
    }

    @Test
    public void testWhenNumberOfSubnetsIsNotPowerOfTwo() {

        var subnets = Subnetting.split(new IPAddressString("10.0.1.0/24").getAddress(), 5);

        assertThat(subnets, hasSize(5));

        //Five subnets will round up to 8 as 2^3=8, so prefix length of 3 will be used
        var firstSubnet = subnets.get(0);
        var secondSubnet = subnets.get(1);
        var thirdSubnet = subnets.get(2);
        var fourthSubnet = subnets.get(3);
        var fifthSubnet = subnets.get(4);

        assertThat(firstSubnet.toString(), equalTo("10.0.1.0/27"));
        assertThat(secondSubnet.toString(), equalTo("10.0.1.32/27"));
        assertThat(thirdSubnet.toString(), equalTo("10.0.1.64/27"));
        assertThat(fourthSubnet.toString(), equalTo("10.0.1.96/27"));
        assertThat(fifthSubnet.toString(), equalTo("10.0.1.128/27"));

        subnets.forEach(s -> assertThat(s.getCount(), equalTo(new BigInteger("32"))));
    }
}
