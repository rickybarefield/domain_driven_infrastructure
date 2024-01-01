package com.appagility.domaindriveninfrastructure;

import com.appagility.domaindriveninfrastructure.aws.AwsNetwork;
import com.appagility.domaindriveninfrastructure.base.ContextNamingStrategy;
import com.pulumi.Pulumi;
import com.pulumi.aws.ec2.Vpc;
import com.pulumi.core.Output;

public class Networking {


    public static void main(String[] args) {

        Pulumi.run(ctx -> {

            var inputs = NetworkingInputs.deserialize(ctx);
            var contextNamingStrategy = new ContextNamingStrategy(ctx);

            var network =
                    AwsNetwork.builder()
                            .namingStrategy(contextNamingStrategy)
                            .name("main")
                            .tierNames("data", "compute", "web")
                            .networkRange(inputs.getNetworkCidrRange())
                            .buildWithEvenlySplitSubnetsAcrossAllAvailabilityZones();


            network.defineInfrastructure();

            var dataSubnets = network.getSubnets("data");
            var computeSubnets = network.getSubnets("compute");
            var webSubnets = network.getSubnets("web");


            NetworkingOutputs.create(dataSubnets, computeSubnets, webSubnets).serialize(ctx);

            ctx.export("subnetSize", Output.of(dataSubnets.get(0).getRange().getCount().toString()));
        });
    }
}