package com.appagility.domaindriveninfrastructure;

import com.appagility.domaindriveninfrastructure.base.ContextNamingStrategy;
import com.appagility.domaindriveninfrastructure.data.DataIndexerMother;
import com.appagility.domaindriveninfrastructure.data.PostgresMother;
import com.pulumi.Pulumi;
import com.appagility.domaindriveninfrastructure.aws.AwsFactory;
import com.pulumi.core.Output;

public class DataTier {

    public static void main(String[] args) {

        Pulumi.run(ctx -> {

            AwsFactory cloudProviderFactory = new AwsFactory(new ContextNamingStrategy(ctx));

            PostgresMother postgresMother = new PostgresMother(cloudProviderFactory);
            DataIndexerMother indexerMother = new DataIndexerMother(cloudProviderFactory,
                    postgresMother.getEndpoint());

            var dataTier = cloudProviderFactory.tierBuilder()
                    .name("data")
                    .component(indexerMother.getComponent())
                    .component(postgresMother.getComponent())
                    .exposes(postgresMother.getEndpoint(), 7412)
                    .build();

            dataTier.defineInfrastructure();


            ctx.export("example", Output.of("example"));
        });
    }

}
