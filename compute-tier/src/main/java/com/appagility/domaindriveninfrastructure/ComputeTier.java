package com.appagility.domaindriveninfrastructure;

import com.appagility.domaindriveninfrastructure.aws.AwsFactory;
import com.appagility.domaindriveninfrastructure.base.ContextNamingStrategy;
import com.pulumi.Pulumi;

public class ComputeTier {

    public static void main(String[] args) {

        Pulumi.run(ctx -> {

            var namingStrategy = new ContextNamingStrategy(ctx);
            var cloudProviderFactory = new AwsFactory(namingStrategy);

            var businessServiceMother = new BusinessServiceMother(cloudProviderFactory);

            var computeTier = cloudProviderFactory.tierBuilder()
                    .name("compute")
                    .component(businessServiceMother.getComponent())
                    .exposes(businessServiceMother.getEndpoint(), 9001)
                    .build();

            computeTier.defineInfrastructure();
        });
    }
}
