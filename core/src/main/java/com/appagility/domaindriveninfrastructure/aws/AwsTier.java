package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.base.Endpoint;
import com.appagility.domaindriveninfrastructure.base.Tier;
import com.pulumi.aws.ec2.Ec2Functions;
import com.pulumi.aws.ec2.inputs.GetSubnetsArgs;
import com.pulumi.aws.ec2.inputs.GetSubnetsFilterArgs;
import lombok.Builder;
import lombok.Singular;

import java.util.List;
import java.util.stream.Stream;

public class AwsTier extends Tier<AwsComponent> {

    @Builder
    public AwsTier(String name, @Singular("exposes") List<Endpoint> exposes, @Singular List<AwsComponent> components) {

        super(name, exposes, components);
    }

    @Override
    public void defineInfrastructure() {

        var subnetNames = Stream.of("a", "b", "c").map(c -> this.name + "-" + c).toList();

        var subnets = Ec2Functions.getSubnets(GetSubnetsArgs.builder()
                .filters(GetSubnetsFilterArgs.builder().name("tag:Name").values(subnetNames).build()).build());


        this.components.forEach(c -> c.defineInfrastructure(subnets));
    }

    public static class AwsTierBuilder implements Tier.TierBuilder<AwsComponent> {


    }
}
