package com.appagility.domaindriveninfrastructure.base;

public interface Securable<TEndpoint extends Endpoint> {

    void allowTcpAccessTo(TEndpoint endpoint);
}
