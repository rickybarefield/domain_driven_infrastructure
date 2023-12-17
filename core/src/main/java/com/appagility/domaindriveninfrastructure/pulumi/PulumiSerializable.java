package com.appagility.domaindriveninfrastructure.pulumi;

import com.pulumi.Context;

public interface PulumiSerializable {

    void serializeToContext(Context context);
    void deserializeFromContext(Context context);
}
