package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.base.NamingStrategy;
import com.pulumi.aws.ec2.SecurityGroup;
import com.pulumi.aws.ec2.SecurityGroupRule;
import com.pulumi.aws.ec2.SecurityGroupRuleArgs;
import com.pulumi.aws.ec2.enums.ProtocolType;

public interface AwsSecurable {

    SecurityGroup getSecurityGroup();

    String getName();

    NamingStrategy getNamingStrategy();

    default void allowTcpAccessTo(AwsSecurable other, int port) {

        new SecurityGroupRule(getNamingStrategy().generateName(other.getName() + "-" +  port),
                SecurityGroupRuleArgs.builder()
                        .type("egress")
                        .fromPort(port)
                        .toPort(port)
                        .protocol(ProtocolType.TCP)
                        .securityGroupId(getSecurityGroup().id())
                        .sourceSecurityGroupId(other.getSecurityGroup().id())
                        .build());

        new SecurityGroupRule(getNamingStrategy().generateName(getName() + "-" + port),
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
