package com.appagility.domaindriveninfrastructure;

import com.appagility.domaindriveninfrastructure.aws.AwsInstanceBasedComponent;
import com.appagility.domaindriveninfrastructure.aws.InternalAwsEndpoint;
import com.appagility.domaindriveninfrastructure.aws.ReferencedAwsEndpoint;
import com.appagility.domaindriveninfrastructure.base.LoadBalancedEndpoint;
import com.pulumi.Context;
import com.pulumi.resources.StackReference;
import lombok.Getter;

public class DataStackOutputs {

    private final String CONTEXT_KEY = "outputs";

    @Getter
    private final ReferencedAwsEndpoint postgresEndpoint;

    public DataStackOutputs(ReferencedAwsEndpoint postgresEndpoint) {

        this.postgresEndpoint = postgresEndpoint;
    }

    public DataStackOutputs(LoadBalancedEndpoint<AwsInstanceBasedComponent, InternalAwsEndpoint> loadBalancedEndpoint) {

        postgresEndpoint = new ReferencedAwsEndpoint(
                loadBalancedEndpoint.target().getName(),
                loadBalancedEndpoint.target().getPort(),
                loadBalancedEndpoint.target().getSecurityGroupId());
    }

    public void serialize(Context context) {

        postgresEndpoint.serialize("postgresEndpoint", context);
    }

    public static DataStackOutputs deserialize(Context context) {

        var stackReference = new StackReference("organization/ddi-data/" + context.stackName());
        var postgresEndpoint = ReferencedAwsEndpoint.deserialize("postgresEndpoint", stackReference);
        return new DataStackOutputs(postgresEndpoint);
    }
}
