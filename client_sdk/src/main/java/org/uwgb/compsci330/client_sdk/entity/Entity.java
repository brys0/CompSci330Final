package org.uwgb.compsci330.client_sdk.entity;

import lombok.Getter;
import org.uwgb.compsci330.client_sdk.Client;

public abstract class Entity {
    @Getter
    protected final Client client;

    protected Entity(Client client) {
        this.client = client;
    }
}