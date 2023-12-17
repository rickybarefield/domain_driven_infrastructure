package com.appagility.domaindriveninfrastructure.data;

import com.appagility.domaindriveninfrastructure.aws.AwsComponent;
import com.appagility.domaindriveninfrastructure.aws.AwsFactory;
import com.appagility.domaindriveninfrastructure.base.Endpoint;
import com.appagility.domaindriveninfrastructure.base.GoldenAmi;
import com.appagility.domaindriveninfrastructure.base.Protocol;


public  class PostgresMother {

    private final AwsComponent component;
    private final Endpoint endpoint;

    public PostgresMother(AwsFactory cloudProviderFactory) {

        endpoint = cloudProviderFactory.endpointBuilder()
                .protocol(Protocol.TCP)
                .port(5432)
                .build();

        component = cloudProviderFactory.componentBuilder()
                .basedOn(GoldenAmi.BASE)
                .scalingApproach(cloudProviderFactory.scalingApproachBuilder().minInstances(3).maxInstances(3).build())
                .shortCode("psgres")
                .exposes(endpoint)
                .build();
    }

    public AwsComponent component() {

        return component;
    }

    public Endpoint endpoint() {

        return endpoint;
    }
}
