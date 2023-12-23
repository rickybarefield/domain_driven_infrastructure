package com.appagility.domaindriveninfrastructure.aws;

import com.pulumi.aws.ec2.SecurityGroup;
import com.pulumi.aws.ec2.SecurityGroupRule;
import com.pulumi.aws.ec2.SecurityGroupRuleArgs;
import com.pulumi.aws.ec2.enums.ProtocolType;

public interface AwsSecurable {

    SecurityGroup getSecurityGroup();

    String getName();

    default void allowTcpAccessTo(AwsSecurable other, int port) {

        new SecurityGroupRule("to-" + other.getName() + "-on-" + port,
                SecurityGroupRuleArgs.builder()
                        .type("egress")
                        .fromPort(port)
                        .toPort(port)
                        .protocol(ProtocolType.TCP)
                        .securityGroupId(getSecurityGroup().id())
                        .sourceSecurityGroupId(other.getSecurityGroup().id())
                        .build());

        new SecurityGroupRule("from-" + getName() + "-on-" + port,
                SecurityGroupRuleArgs.builder()
                        .type("ingress")
                        .fromPort(port)
                        .toPort(port)
                        .protocol(ProtocolType.TCP)
                        .securityGroupId(other.getSecurityGroup().id())
                        .sourceSecurityGroupId(getSecurityGroup().id())
                        .build());
    }
}
