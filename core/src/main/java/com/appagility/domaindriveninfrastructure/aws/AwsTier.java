package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.base.Endpoint;
import com.appagility.domaindriveninfrastructure.base.Protocol;
import com.appagility.domaindriveninfrastructure.base.Tier;
import com.pulumi.aws.ec2.Ec2Functions;
import com.pulumi.aws.ec2.inputs.GetSubnetsArgs;
import com.pulumi.aws.ec2.inputs.GetSubnetsFilterArgs;
import com.pulumi.aws.lb.*;
import com.pulumi.aws.lb.inputs.ListenerDefaultActionArgs;
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

        if(theTierExposesARawTcpEndpoint()) {

            var nlb = createAnNlb();

            //TODO not convinced this logic shouldn't be delegated
            addAllExposedRawTcpEndpointsToTheNlb(nlb);
        }

    }

    private boolean theTierExposesARawTcpEndpoint() {

        return exposes.stream().anyMatch(e -> e.target().getProtocol() == Protocol.TCP);
    }

    private LoadBalancer createAnNlb() {

        return new LoadBalancer(name, LoadBalancerArgs.builder().loadBalancerType("network").build());
    }

    private void addAllExposedRawTcpEndpointsToTheNlb(LoadBalancer nlb) {

        var tcpExposedEndpoints = exposes.stream().filter(e -> e.target().getProtocol() == Protocol.TCP);

        tcpExposedEndpoints.forEach(tcpLoadBalancedEndpoint -> {

            var component = findComponentForEndpoint(tcpLoadBalancedEndpoint.target());

            new Listener(name + "-some-component", ListenerArgs.builder()
                    .port(tcpLoadBalancedEndpoint.port())
                    .loadBalancerArn(nlb.arn())
                    .defaultActions(ListenerDefaultActionArgs.builder()
                            .type("forward")
                            .targetGroupArn(component.getTargetGroupArn()).build())
                    .build());
        });
    }

    private AwsInstanceBasedComponent findComponentForEndpoint(Endpoint endpoint) {

        return components.stream().filter(AwsInstanceBasedComponent.class::isInstance)
                .map(c -> (AwsInstanceBasedComponent)c)
                .filter(c -> c.doesExpose(endpoint)).findFirst()
                .orElseThrow(() -> new RuntimeException("No components serve given endpoint"));
    }

    public static class AwsTierBuilder implements Tier.TierBuilder<AwsComponent> {


    }
}
