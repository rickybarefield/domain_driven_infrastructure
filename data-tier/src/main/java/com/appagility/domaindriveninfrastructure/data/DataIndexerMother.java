package com.appagility.domaindriveninfrastructure.data;

import com.appagility.domaindriveninfrastructure.aws.AwsEndpoint;
import com.appagility.domaindriveninfrastructure.aws.AwsFactory;
import com.appagility.domaindriveninfrastructure.aws.AwsInstanceBasedComponent;
import com.appagility.domaindriveninfrastructure.base.InternalEndpoint;
import com.appagility.domaindriveninfrastructure.base.GoldenAmi;
import lombok.Getter;

public class DataIndexerMother {

    @Getter
    private final AwsInstanceBasedComponent component;

    public DataIndexerMother(AwsFactory awsFactory, AwsEndpoint dataEndpoint) {

        this.component = awsFactory.componentBuilder()
                .basedOn(GoldenAmi.BASE)
                .shortCode("dtinx")
                .accesses(dataEndpoint)
                .scalingApproach(awsFactory.scalingApproachBuilder().minInstances(1).maxInstances(1).build())
                .build();
    }
}
