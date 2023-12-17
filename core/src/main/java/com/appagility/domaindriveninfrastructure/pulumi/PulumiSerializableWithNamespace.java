package com.appagility.domaindriveninfrastructure.pulumi;

import com.pulumi.Context;

public interface PulumiSerializableWithNamespace {

    void serializeToContext(Context context, String namespace);
    void deserializeFromContext(Context context, String namespace);
}
