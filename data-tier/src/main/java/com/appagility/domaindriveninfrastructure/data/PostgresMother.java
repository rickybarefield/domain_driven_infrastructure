package com.appagility.domaindriveninfrastructure.data;

import com.appagility.domaindriveninfrastructure.aws.InternalAwsEndpoint;
import com.appagility.domaindriveninfrastructure.aws.AwsFactory;
import com.appagility.domaindriveninfrastructure.aws.AwsInstanceBasedComponent;
import com.appagility.domaindriveninfrastructure.base.GoldenAmi;
import com.appagility.domaindriveninfrastructure.base.Protocol;
import lombok.Getter;


public  class PostgresMother {

    @Getter
    private final AwsInstanceBasedComponent component;

    @Getter
    private final InternalAwsEndpoint endpoint;

    public PostgresMother(AwsFactory cloudProviderFactory) {

        endpoint = cloudProviderFactory.endpointBuilder()
                .protocol(Protocol.TCP)
                .port(5432)
                .build();

        var clusteringEndpoint = cloudProviderFactory.endpointBuilder()
                .protocol(Protocol.TCP)
                .port(5382)
                .build();

        component = cloudProviderFactory.componentBuilder()
                .basedOn(GoldenAmi.BASE)
                .scalingApproach(cloudProviderFactory.scalingApproachBuilder().minInstances(3).maxInstances(3).build())
                .shortCode("psgres")
                .exposes(endpoint)
                .exposes(clusteringEndpoint)
                .accesses(clusteringEndpoint) //Components should be able to access endpoints of others in ASG
                .build();
    }
}
