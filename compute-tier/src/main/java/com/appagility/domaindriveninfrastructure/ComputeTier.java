package com.appagility.domaindriveninfrastructure;

import com.appagility.domaindriveninfrastructure.aws.AwsEndpoint;
import com.appagility.domaindriveninfrastructure.aws.AwsFactory;
import com.appagility.domaindriveninfrastructure.base.ContextNamingStrategy;
import com.appagility.domaindriveninfrastructure.base.InternalEndpoint;
import com.appagility.domaindriveninfrastructure.base.Protocol;
import com.pulumi.Pulumi;
import com.pulumi.resources.StackReference;

public class ComputeTier {

    public static void main(String[] args) {

        Pulumi.run(ctx -> {

            var namingStrategy = new ContextNamingStrategy(ctx);
            var cloudProviderFactory = new AwsFactory(namingStrategy);

            var dataStack = new StackReference("organization/ddi-data/" + ctx.stackName());

//            var dataStackOutputs = DataStackOutputs.deserialize(dataStack);
//
//            InternalEndpoint postgresEndpoint = dataStackOutputs.getPostgresEndpoint();

            var businessServiceMother = new BusinessServiceMother(cloudProviderFactory, new AwsEndpoint(Protocol.TCP, 5672));

            var computeTier = cloudProviderFactory.tierBuilder()
                    .name("compute")
                    .component(businessServiceMother.getComponent())
                    .exposes(businessServiceMother.getEndpoint(), 9001)
                    .build();

            computeTier.defineInfrastructure();
        });
    }
}
