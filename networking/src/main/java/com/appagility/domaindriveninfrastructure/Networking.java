package com.appagility.domaindriveninfrastructure;

import com.appagility.domaindriveninfrastructure.aws.AwsNetwork;
import com.appagility.domaindriveninfrastructure.base.Network;
import com.pulumi.Pulumi;
import com.pulumi.core.Output;

public class Networking {


    public static void main(String[] args) {

        Pulumi.run(ctx -> {

            var inputs = NetworkingInputs.deserialize(ctx);

            var network =
                    AwsNetwork.builder()
                            .withEvenlySplitSubnets(inputs.getNetworkCidrRange(), 9)
                            .build();

            ctx.export("test", Output.of(inputs.getNetworkCidrRange().toString()));

        });
    }
}