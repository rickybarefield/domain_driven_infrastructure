package com.appagility.domaindriveninfrastructure.base;


import com.appagility.Builder;
import lombok.Getter;

public abstract class Endpoint {

    @Getter
    private final Protocol protocol;

    @Getter
    private final int port;

    public Endpoint(Protocol protocol, int port) {

        this.protocol = protocol;
        this.port = port;
    }

    public interface EndpointBuilder extends Builder<Endpoint> {

        EndpointBuilder port(int port);

        EndpointBuilder protocol(Protocol protocol);
    }
}
