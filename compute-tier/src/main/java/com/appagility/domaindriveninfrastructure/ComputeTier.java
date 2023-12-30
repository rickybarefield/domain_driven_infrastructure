package com.appagility.domaindriveninfrastructure;

import com.appagility.domaindriveninfrastructure.aws.AwsFactory;
import com.appagility.domaindriveninfrastructure.base.ContextNamingStrategy;
import com.pulumi.Pulumi;
import com.pulumi.resources.StackReference;

public class ComputeTier {

    public static void main(String[] args) {

        Pulumi.run(ctx -> {

            var namingStrategy = new ContextNamingStrategy(ctx);
            var cloudProviderFactory = new AwsFactory(namingStrategy);

            //Outputs from the data tier can be accessed in a type safe manner
            var dataStackOutputs = DataStackOutputs.deserialize(ctx);

            //Here one of the outputs is passed through as an AwsEndpoint and used to state that the business service needs access
            //to Postgres
            var businessServiceMother = new BusinessServiceMother(cloudProviderFactory, dataStackOutputs.getPostgresEndpoint());

            var computeTier = cloudProviderFactory.tierBuilder()
                    .name("compute")
                    .component(businessServiceMother.getComponent())
                    .exposes(businessServiceMother.getEndpoint(), 9001)
                    .build();

            computeTier.defineInfrastructure();
        });
    }
}
