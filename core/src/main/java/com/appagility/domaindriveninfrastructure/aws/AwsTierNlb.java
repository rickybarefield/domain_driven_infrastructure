package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.aws.AwsTier.LoadBalancedEndpointWithMatchingComponent;
import com.appagility.domaindriveninfrastructure.base.Protocol;
import com.appagility.domaindriveninfrastructure.base.NamingStrategy;
import com.pulumi.aws.ec2.SecurityGroup;
import com.pulumi.aws.lb.Listener;
import com.pulumi.aws.lb.ListenerArgs;
import com.pulumi.aws.lb.LoadBalancer;
import com.pulumi.aws.lb.LoadBalancerArgs;
import com.pulumi.aws.lb.inputs.ListenerDefaultActionArgs;
import lombok.Getter;

import java.util.List;

public class AwsTierNlb implements AwsSecurable {

    @Getter
    private final NamingStrategy namingStrategy;
    private final SecurityGroup securityGroup;

    @Override
    public SecurityGroup getSecurityGroup() {
        return securityGroup;
    }

    private final LoadBalancer loadBalancer;

    @Override
    public String getName() {

        return "nlb";
    }

    public AwsTierNlb(NamingStrategy namingStrategy, List<LoadBalancedEndpointWithMatchingComponent> exposes) {

        this.namingStrategy = namingStrategy;

        securityGroup = new SecurityGroup(namingStrategy.generateName(getName()));

        loadBalancer = new LoadBalancer(namingStrategy.generateName(getName()), LoadBalancerArgs.builder().loadBalancerType("network")
                .securityGroups(securityGroup.id().applyValue(List::of)).build());

        addAllExposedEndpointsToTheNlb(exposes);
    }

    private void addAllExposedEndpointsToTheNlb(List<LoadBalancedEndpointWithMatchingComponent> exposes) {

        exposes.forEach(exposedEndpointAndComponent -> {

            String name = namingStrategy.generateName(
                    exposedEndpointAndComponent.component().getName() + "-" + exposedEndpointAndComponent.endpoint().port());

            new Listener(name,
                    ListenerArgs.builder()
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
