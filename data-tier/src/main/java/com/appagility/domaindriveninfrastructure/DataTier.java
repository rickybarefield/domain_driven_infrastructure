package com.appagility.domaindriveninfrastructure;

import com.appagility.domaindriveninfrastructure.data.PostgresMother;
import com.pulumi.Pulumi;
import com.appagility.domaindriveninfrastructure.aws.AwsFactory;

public class DataTier {

    public static void main(String[] args) {

        Pulumi.run(ctx -> {

            AwsFactory cloudProviderFactory = new AwsFactory();

            PostgresMother postgresMother = new PostgresMother(cloudProviderFactory);

            var dataTier = cloudProviderFactory.tierBuilder()
                    .name("data")
                    .component(postgresMother.component())
                    .exposes(postgresMother.endpoint())
                    .build();


            dataTier.defineInfrastructure();


//            ctx.export("exampleOutput", Output.of("example"));
        });
    }

}
