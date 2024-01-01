package com.appagility.domaindriveninfrastructure.aws;

import com.appagility.domaindriveninfrastructure.base.Component;
import com.pulumi.aws.ec2.outputs.GetSubnetsResult;
import com.pulumi.core.Output;

import java.util.List;

public interface AwsComponent extends Component {
    void defineInfrastructure(Output<List<String>> subnetIds);

    String getName();
}
