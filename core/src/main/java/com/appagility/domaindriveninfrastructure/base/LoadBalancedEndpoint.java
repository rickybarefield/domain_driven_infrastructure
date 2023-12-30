package com.appagility.domaindriveninfrastructure.base;

public record LoadBalancedEndpoint<
        TComponent extends Component,
        TEndpoint extends InternalEndpoint<TComponent>>(TEndpoint target, int port) {

}

