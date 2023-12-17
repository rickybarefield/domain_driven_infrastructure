package com.appagility.domaindriveninfrastructure.base;


import com.appagility.Builder;

public abstract class Endpoint {

    private final String protocol;
    private final int port;

    public Endpoint(String protocol, int port) {

        this.protocol = protocol;
        this.port = port;
    }

    public interface EndpointBuilder extends Builder<Endpoint> {

        EndpointBuilder port(int port);

        EndpointBuilder protocol(String protocol);
    }
}
