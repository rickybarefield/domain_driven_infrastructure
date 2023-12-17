package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.base.Endpoint;
import com.appagility.domaindriveninfrastructure.base.Protocol;
import com.appagility.domaindriveninfrastructure.base.Tier;
import com.pulumi.aws.ec2.Ec2Functions;
import com.pulumi.aws.ec2.inputs.GetSubnetsArgs;
import com.pulumi.aws.ec2.inputs.GetSubnetsFilterArgs;
import com.pulumi.aws.lb.LoadBalancer;
import com.pulumi.aws.lb.LoadBalancerArgs;
import com.pulumi.aws.lb.TargetGroup;
import com.pulumi.aws.lb.TargetGroupArgs;
import lombok.Builder;
import lombok.Singular;

import java.util.List;
import java.util.stream.Stream;

public class AwsTier extends Tier<AwsComponent> {

    @Builder
    public AwsTier(String name, @Singular("exposes") List<LoadBalancedEndpoint> exposes, @Singular List<AwsComponent> components) {

        super(name, exposes, components);
    }

    @Override
    public void defineInfrastructure() {

        var subnetNames = Stream.of("a", "b", "c").map(c -> this.name + "-" + c).toList();

        var subnets = Ec2Functions.getSubnets(GetSubnetsArgs.builder()
                .filters(GetSubnetsFilterArgs.builder().name("tag:Name").values(subnetNames).build()).build());

        this.components.forEach(c -> c.defineInfrastructure(subnets));

        if(exposes.stream().anyMatch(e -> e.target().getProtocol() == Protocol.TCP)) {

            var nlb = new LoadBalancer(name, LoadBalancerArgs.builder().loadBalancerType("network").build());

            var tcpExposedEndpoints = exposes.stream().filter(e -> e.target().getProtocol() == Protocol.TCP);

            tcpExposedEndpoints.forEach(tcpLoadBalancedEndpoint -> {

                //TODO
            });
        }

    }

    public static class AwsTierBuilder implements Tier.TierBuilder<AwsComponent> {


    }

}
