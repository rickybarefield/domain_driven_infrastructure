package com.appagility.domaindriveninfrastructure;

import com.appagility.domaindriveninfrastructure.aws.AwsFactory;
import com.appagility.domaindriveninfrastructure.aws.ReferencedAwsEndpoint;
import com.appagility.domaindriveninfrastructure.base.ContextNamingStrategy;
import com.appagility.domaindriveninfrastructure.base.LoadBalancedEndpoint;
import com.appagility.domaindriveninfrastructure.data.DataIndexerMother;
import com.appagility.domaindriveninfrastructure.data.PostgresMother;
import com.pulumi.Pulumi;
import com.pulumi.core.Output;

public class DataTier {

    public static void main(String[] args) {

        Pulumi.run(ctx -> {

            AwsFactory cloudProviderFactory = new AwsFactory(new ContextNamingStrategy(ctx));

            PostgresMother postgresMother = new PostgresMother(cloudProviderFactory);
            DataIndexerMother indexerMother = new DataIndexerMother(cloudProviderFactory,
                    postgresMother.getEndpoint());

            var loadBalancedPostgresEndpoint = new LoadBalancedEndpoint<>(postgresMother.getEndpoint(), 7412);

            var dataTier = cloudProviderFactory.tierBuilder()
                    .name("data")
                    .component(indexerMother.getComponent())
                    .component(postgresMother.getComponent())
                    .exposes(loadBalancedPostgresEndpoint)
                    .build();

            dataTier.defineInfrastructure();

            //Outputs are created from objects used to define stack
            new DataStackOutputs(loadBalancedPostgresEndpoint).serialize(ctx);
        });
    }

}
