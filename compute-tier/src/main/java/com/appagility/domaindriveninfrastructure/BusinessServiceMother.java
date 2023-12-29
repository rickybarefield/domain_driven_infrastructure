package com.appagility.domaindriveninfrastructure;

import com.appagility.domaindriveninfrastructure.aws.AwsFactory;
import com.appagility.domaindriveninfrastructure.aws.AwsInstanceBasedComponent;
import com.appagility.domaindriveninfrastructure.base.Endpoint;
import com.appagility.domaindriveninfrastructure.base.GoldenAmi;
import com.appagility.domaindriveninfrastructure.base.Protocol;
import lombok.Getter;

public class BusinessServiceMother {

    @Getter
    private final AwsInstanceBasedComponent component;

    @Getter
    private final Endpoint<AwsInstanceBasedComponent> endpoint;

    public BusinessServiceMother(AwsFactory awsFactory) {

        endpoint = awsFactory.endpointBuilder()
                .protocol(Protocol.TCP)
                .port(8080)
                .build();

        component = awsFactory.componentBuilder()
                .basedOn(GoldenAmi.BASE)
                .shortCode("bussvc")
                .scalingApproach(awsFactory.scalingApproachBuilder().minInstances(2).maxInstances(2).build())
                .exposes(endpoint)
                .build();
    }
}
