package com.appagility.domaindriveninfrastructure;

import com.appagility.domaindriveninfrastructure.aws.AwsEndpoint;
import com.appagility.domaindriveninfrastructure.aws.AwsFactory;
import com.appagility.domaindriveninfrastructure.aws.ReferencedAwsEndpoint;
import com.appagility.domaindriveninfrastructure.base.ContextNamingStrategy;
import com.pulumi.Pulumi;
import com.pulumi.core.Output;
import com.pulumi.resources.StackReference;

public class ComputeTier {

    public static void main(String[] args) {

        Pulumi.run(ctx -> {

            var namingStrategy = new ContextNamingStrategy(ctx);
            var cloudProviderFactory = new AwsFactory(namingStrategy);

            var dataStack = new StackReference("organization/ddi-data/" + ctx.stackName());

            //var dataStackOutputs = DataStackOutputs.deserialize(dataStack);

            AwsEndpoint postgresEndpoint = new ReferencedAwsEndpoint(5632, "postgres", Output.of("some-id")); //dataStackOutputs.getPostgresEndpoint();

            var businessServiceMother = new BusinessServiceMother(cloudProviderFactory, postgresEndpoint);

            var computeTier = cloudProviderFactory.tierBuilder()
                    .name("compute")
                    .component(businessServiceMother.getComponent())
                    .exposes(businessServiceMother.getEndpoint(), 9001)
                    .build();

            computeTier.defineInfrastructure();
        });
    }
}
