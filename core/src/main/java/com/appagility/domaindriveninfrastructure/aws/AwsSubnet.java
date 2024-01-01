package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.MayBecome;
import com.appagility.domaindriveninfrastructure.base.NamingStrategy;
import com.pulumi.aws.ec2.Subnet;
import com.pulumi.aws.ec2.SubnetArgs;
import com.pulumi.aws.ec2.Vpc;
import com.pulumi.core.Output;
import inet.ipaddr.IPAddress;
import lombok.Getter;

public class AwsSubnet {

    @Getter
    private final IPAddress range;
    private final String name;
    private final NamingStrategy namingStrategy;

    @Getter
    private final String availabilityZone;

    @Getter
    private final String tierName;

    private final MayBecome<Subnet> subnet = MayBecome.empty("subnet");

    public AwsSubnet(NamingStrategy namingStrategy, String tierName, IPAddress range, String availabilityZone) {

        this.range = range;
        this.namingStrategy = namingStrategy;
        this.availabilityZone = availabilityZone;
        this.tierName = tierName;
        this.name = tierName + "-" + availabilityZone;
    }

    public void defineInfrastructure(Vpc vpc) {

        subnet.set(new com.pulumi.aws.ec2.Subnet(namingStrategy.generateName(name),
                SubnetArgs.builder()
                        .vpcId(vpc.id())
                        .cidrBlock(range.toAddressString().toString())
                        .availabilityZone(availabilityZone).build()));
    }

    public Output<String> getId() {

        return subnet.get().id();
    }
}
