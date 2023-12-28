package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.base.NamingStrategy;
import com.appagility.domaindriveninfrastructure.base.Protocol;
import com.appagility.domaindriveninfrastructure.base.Tier;
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

    public AwsTierNlb(NamingStrategy namingStrategy, List<Tier.LoadBalancedEndpoint<AwsInstanceBasedComponent>> exposes) {

        this.namingStrategy = namingStrategy;

        securityGroup = new SecurityGroup(namingStrategy.generateName(getName()));

        loadBalancer = new LoadBalancer(namingStrategy.generateName(getName()), LoadBalancerArgs.builder().loadBalancerType("network")
                .securityGroups(securityGroup.id().applyValue(List::of)).build());

        addAllExposedEndpointsToTheNlb(exposes);
    }

    private void addAllExposedEndpointsToTheNlb(List<Tier.LoadBalancedEndpoint<AwsInstanceBasedComponent>> exposes) {

        exposes.forEach(exposedEndpoint -> {

            String name = namingStrategy.generateName(
                    exposedEndpoint.target().getComponent().getName() + "-" + exposedEndpoint.port());

            new Listener(name,
                    ListenerArgs.builder()
                    .port(exposedEndpoint.port())
                    .loadBalancerArn(loadBalancer.arn())
                    .defaultActions(ListenerDefaultActionArgs.builder()
                            .type("forward")
                            .targetGroupArn(exposedEndpoint.target().getComponent().getTargetGroupArn()).build())
                    .build());

            allowTcpAccessTo(exposedEndpoint.target().getComponent(), exposedEndpoint.port());
        });
    }


    public static List<Tier.LoadBalancedEndpoint<AwsInstanceBasedComponent>> filterForNlb(List<Tier.LoadBalancedEndpoint<AwsInstanceBasedComponent>> exposedEndpoints) {

        return exposedEndpoints.stream().filter(
                e -> e.target().getProtocol() == Protocol.TCP).toList();
    }
}
