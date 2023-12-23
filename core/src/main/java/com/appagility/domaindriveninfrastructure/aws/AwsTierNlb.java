package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.aws.AwsTier.LoadBalancedEndpointWithMatchingComponent;
import com.appagility.domaindriveninfrastructure.base.Protocol;
import com.pulumi.aws.ec2.SecurityGroup;
import com.pulumi.aws.lb.Listener;
import com.pulumi.aws.lb.ListenerArgs;
import com.pulumi.aws.lb.LoadBalancer;
import com.pulumi.aws.lb.LoadBalancerArgs;
import com.pulumi.aws.lb.inputs.ListenerDefaultActionArgs;

import java.util.List;

public class AwsTierNlb implements AwsSecurable {

    private String name;
    private final SecurityGroup securityGroup;

    @Override
    public SecurityGroup getSecurityGroup() {
        return securityGroup;
    }

    private final LoadBalancer loadBalancer;

    @Override
    public String getName() {

        return name;
    }

    public AwsTierNlb(String tierName, List<LoadBalancedEndpointWithMatchingComponent> exposes) {

        name = tierName + "-nlb";

        securityGroup = new SecurityGroup(getName() + "-nlb");

        loadBalancer = new LoadBalancer(getName(), LoadBalancerArgs.builder().loadBalancerType("network")
                .securityGroups(securityGroup.id().applyValue(List::of)).build());

        addAllExposedEndpointsToTheNlb(exposes);
    }

    private void addAllExposedEndpointsToTheNlb(List<LoadBalancedEndpointWithMatchingComponent> exposes) {

        exposes.forEach(exposedEndpointAndComponent -> {

            new Listener(getName() + "-some-component", ListenerArgs.builder()
                    .port(exposedEndpointAndComponent.endpoint().port())
                    .loadBalancerArn(loadBalancer.arn())
                    .defaultActions(ListenerDefaultActionArgs.builder()
                            .type("forward")
                            .targetGroupArn(exposedEndpointAndComponent.component().getTargetGroupArn()).build())
                    .build());

            allowTcpAccessTo(exposedEndpointAndComponent.component(), exposedEndpointAndComponent.endpoint().port());
        });
    }


    public static List<LoadBalancedEndpointWithMatchingComponent> filterForNlb(List<LoadBalancedEndpointWithMatchingComponent> exposedEndpoints) {

        return exposedEndpoints.stream().filter(
                e -> e.endpoint().target().getProtocol() == Protocol.TCP).toList();
    }
}
