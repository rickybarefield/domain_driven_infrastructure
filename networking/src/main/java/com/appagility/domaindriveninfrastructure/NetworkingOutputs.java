package com.appagility.domaindriveninfrastructure;

import com.appagility.domaindriveninfrastructure.aws.AwsSubnet;
import com.appagility.domaindriveninfrastructure.aws.ReferencedAwsSubnet;
import com.pulumi.Context;
import com.pulumi.core.Output;
import com.pulumi.resources.StackReference;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.List;

public class NetworkingOutputs {

    @Getter
    private final List<ReferencedAwsSubnet> dataSubnets;

    @Getter
    private final List<ReferencedAwsSubnet> computeSubnets;

    @Getter
    private final List<ReferencedAwsSubnet> webSubnets;

    public NetworkingOutputs(List<ReferencedAwsSubnet> dataSubnets, List<ReferencedAwsSubnet> computeSubnets, List<ReferencedAwsSubnet> webSubnets) {

        this.dataSubnets = dataSubnets;
        this.computeSubnets = computeSubnets;
        this.webSubnets = webSubnets;
    }

    static NetworkingOutputs create(List<AwsSubnet> dataSubnets, List<AwsSubnet> computeSubnets, List<AwsSubnet> webSubnets) {

        return new NetworkingOutputs(dataSubnets.stream().map(ReferencedAwsSubnet::new).toList(),
                computeSubnets.stream().map(ReferencedAwsSubnet::new).toList(),
                webSubnets.stream().map(ReferencedAwsSubnet::new).toList());
    }

    public void serialize(Context context) {


        var dataSubnetIds = Output.all(dataSubnets.stream().map(ReferencedAwsSubnet::getId).toList());
        var computeSubnetIds = Output.all(computeSubnets.stream().map(ReferencedAwsSubnet::getId).toList());
        var webSubnetIds =  Output.all(webSubnets.stream().map(ReferencedAwsSubnet::getId).toList());

        context.export("dataSubnetIds", dataSubnetIds);
        context.export("computeSubnetIds", computeSubnetIds);
        context.export("webSubnetIds", webSubnetIds);
    }

    @SneakyThrows
    public static NetworkingOutputs deserialize(Context context) {

        var stackReference = new StackReference("organization/ddi-networking/" + context.stackName());

        var dataSubnetIds = (List<String>)stackReference.requireValueAsync("dataSubnetIds").get();
        var webSubnetIds = (List<String>)stackReference.requireValueAsync("computeSubnetIds").get();
        var computeSubnetIds = (List<String>)stackReference.requireValueAsync("webSubnetIds").get();

        var dataReferencedSubnets = dataSubnetIds.stream().map(id -> new ReferencedAwsSubnet(Output.of(id))).toList();
        var computeReferencedSubnets = computeSubnetIds.stream().map(id -> new ReferencedAwsSubnet(Output.of(id))).toList();
        var webReferencedSubnets = webSubnetIds.stream().map(id -> new ReferencedAwsSubnet(Output.of(id))).toList();

        return new NetworkingOutputs(dataReferencedSubnets, computeReferencedSubnets, webReferencedSubnets);
    }

}
