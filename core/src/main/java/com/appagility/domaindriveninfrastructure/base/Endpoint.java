package com.appagility.domaindriveninfrastructure.base;


import com.appagility.Builder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class Endpoint<TComponent extends Component> {

    @Getter
    private final Protocol protocol;

    @Getter
    private final int port;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private TComponent component;

    public Endpoint(Protocol protocol, int port) {

        this.protocol = protocol;
        this.port = port;
    }

    public interface EndpointBuilder<TComponent extends Component> extends Builder<Endpoint<TComponent>> {

        EndpointBuilder<TComponent> port(int port);

        EndpointBuilder<TComponent> protocol(Protocol protocol);
    }
}
