package com.appagility.domaindriveninfrastructure.base;

import inet.ipaddr.IPAddress;

import java.util.List;

public class Subnetting {

    public static List<? extends IPAddress> split(IPAddress range, int numberOfSubnets) {

        var prefixIncrement = (int) Math.ceil(log2(numberOfSubnets));

        var adjusted = range.setPrefixLength(range.getPrefixLength() + prefixIncrement, false);

        var subnets = adjusted.prefixBlockStream().limit(numberOfSubnets).toList();

        return subnets;
    }

    private static double log2(int n) {
        return Math.log(n) / Math.log(2);
    }
}
