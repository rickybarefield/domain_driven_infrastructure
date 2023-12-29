package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.base.NamingStrategy;
import com.appagility.domaindriveninfrastructure.base.Securable;
import com.pulumi.aws.ec2.SecurityGroup;
import com.pulumi.aws.ec2.SecurityGroupRule;
import com.pulumi.aws.ec2.SecurityGroupRuleArgs;
import com.pulumi.aws.ec2.enums.ProtocolType;

public interface AwsSecurable extends Securable<AwsEndpoint> {

    SecurityGroup getSecurityGroup();

    String getName();

    NamingStrategy getNamingStrategy();

    @Override
    default void allowTcpAccessTo(AwsEndpoint endpoint) {

        var port = endpoint.getPort();
        var otherSecurityGroupId = endpoint.getComponent().getSecurityGroup().id();
        var otherName = endpoint.getComponent().getName();

        new SecurityGroupRule(getNamingStrategy().generateName(
                "egress-from-" + getName() + "-to-" + otherName + "-" + port),
                SecurityGroupRuleArgs.builder()
                        .type("egress")
                        .fromPort(port)
                        .toPort(port)
                        .protocol(ProtocolType.TCP)
                        .securityGroupId(getSecurityGroup().id())
                        .sourceSecurityGroupId(otherSecurityGroupId)
                        .build());

        new SecurityGroupRule(getNamingStrategy().generateName("ingress-from-" + getName() + "-to-" + otherName + "-" + port),
                SecurityGroupRuleArgs.builder()
                        .type("ingress")
                        .fromPort(port)
                        .toPort(port)
                        .protocol(ProtocolType.TCP)
                        .securityGroupId(otherSecurityGroupId)
                        .sourceSecurityGroupId(getSecurityGroup().id())
                        .build());

    }
}
