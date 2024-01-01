package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.MayBecome;
import com.appagility.domaindriveninfrastructure.base.LoadBalancedEndpoint;
import com.appagility.domaindriveninfrastructure.base.NamingStrategy;
import com.appagility.domaindriveninfrastructure.base.Tier;
import com.pulumi.aws.ec2.Ec2Functions;
import com.pulumi.aws.ec2.inputs.GetSubnetsArgs;
import com.pulumi.aws.ec2.inputs.GetSubnetsFilterArgs;
import com.pulumi.core.Output;
import lombok.Builder;
import lombok.Singular;

import java.util.List;
import java.util.stream.Stream;

public class AwsTier extends Tier<AwsInstanceBasedComponent, InternalAwsEndpoint> {

    private final MayBecome<AwsTierNlb> awsTierNlb = MayBecome.empty("awsTierNlb");

    private final List<ReferencedAwsSubnet> subnets;

    @Builder
    public AwsTier(
            NamingStrategy namingStrategy,
            String name,
            @Singular("exposes") List<LoadBalancedEndpoint<AwsInstanceBasedComponent, InternalAwsEndpoint>> exposes,
            @Singular List<AwsInstanceBasedComponent> components, List<ReferencedAwsSubnet> subnets) {

        super(namingStrategy, exposes, components, name);
        this.subnets = subnets;
    }

    @Override
    public void defineInfrastructure() {

//        var subnetNames = Stream.of("a", "b", "c").map(c -> this.name + "-" + c).toList();
//
//        var subnets = Ec2Functions.getSubnets(GetSubnetsArgs.builder()
//                .filters(GetSubnetsFilterArgs.builder().name("tag:Name").values(subnetNames).build()).build());

        var subnetIds = Output.all(subnets.stream().map(ReferencedAwsSubnet::getId).toList());

        this.components.forEach(c -> c.defineInfrastructure(subnetIds));

        var endpointsForNlb = AwsTierNlb.filterForNlb(exposes);

        if(!endpointsForNlb.isEmpty()) {

            awsTierNlb.set(new AwsTierNlb(namingStrategy, endpointsForNlb, subnetIds));
        }
    }

    public static class AwsTierBuilder implements Tier.TierBuilder<AwsInstanceBasedComponent, InternalAwsEndpoint> {


    }
}
