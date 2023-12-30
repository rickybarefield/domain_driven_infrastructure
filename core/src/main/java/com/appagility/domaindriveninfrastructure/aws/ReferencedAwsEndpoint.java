package com.appagility.domaindriveninfrastructure.aws;

import com.pulumi.Context;
import com.pulumi.core.Output;
import com.pulumi.resources.StackReference;
import lombok.Getter;

import java.util.concurrent.ExecutionException;

public class ReferencedAwsEndpoint implements AwsEndpoint {

    @Getter
    private final String name;

    @Getter
    private final int port;

    @Getter
    private final Output<String> securityGroupId;


    public ReferencedAwsEndpoint(String name, int port, Output<String> securityGroupId) {

        this.name = name;
        this.port = port;
        this.securityGroupId = securityGroupId;
    }

    public static ReferencedAwsEndpoint deserialize(String baseNameInContext, StackReference stackReference) {

        try {

            String name = (String) stackReference.requireValueAsync(baseNameInContext + ".name").get();
            int port = ((Double) stackReference.requireValueAsync(baseNameInContext + ".port").get()).intValue();
            Output<String> securityGroupId = (Output<String>) stackReference.requireOutput(baseNameInContext + ".securityGroupId");


            return new ReferencedAwsEndpoint(name, port, securityGroupId);

        } catch (InterruptedException | ExecutionException e) {

            throw new RuntimeException(e);
        }
    }

    public void serialize(String baseNameInContext, Context context) {

        context.export(baseNameInContext + ".port", Output.of(port));
        context.export(baseNameInContext + ".name", Output.of(name));
        context.export(baseNameInContext + ".securityGroupId", securityGroupId);
    }
}
