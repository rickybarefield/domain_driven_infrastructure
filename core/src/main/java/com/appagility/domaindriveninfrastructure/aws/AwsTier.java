package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.MayBecome;
import com.appagility.domaindriveninfrastructure.base.Tier;
import com.pulumi.aws.ec2.Ec2Functions;
import com.pulumi.aws.ec2.inputs.GetSubnetsArgs;
import com.pulumi.aws.ec2.inputs.GetSubnetsFilterArgs;
import lombok.Builder;
import lombok.Singular;

import java.util.List;
import java.util.stream.Stream;

public class AwsTier extends Tier<AwsComponent> {

    private MayBecome<AwsTierNlb> awsTierNlb = MayBecome.empty("awsTierNlb");

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

        var exposedEndpointsWithComponents = exposes.stream().map(this::matchWithComponent).toList();

        var endpointsForNlb = AwsTierNlb.filterForNlb(exposedEndpointsWithComponents);

        if(!endpointsForNlb.isEmpty()) {

            awsTierNlb.set(new AwsTierNlb(name, endpointsForNlb));
        }
    }

    private LoadBalancedEndpointWithMatchingComponent matchWithComponent(LoadBalancedEndpoint endpoint) {

        return components.stream().filter(AwsInstanceBasedComponent.class::isInstance)
                .map(c -> (AwsInstanceBasedComponent)c)
                .filter(c -> c.doesExpose(endpoint.target()))
                .map(c -> new LoadBalancedEndpointWithMatchingComponent(endpoint, c))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No components serve given endpoint"));
    }

    public static class AwsTierBuilder implements Tier.TierBuilder<AwsComponent> {


    }

    public record LoadBalancedEndpointWithMatchingComponent(LoadBalancedEndpoint endpoint, AwsInstanceBasedComponent component) {

    }
}
