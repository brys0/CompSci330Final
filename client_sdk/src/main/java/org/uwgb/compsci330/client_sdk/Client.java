package org.uwgb.compsci330.client_sdk;

import lombok.Getter;
import org.uwgb.compsci330.client_sdk.http.HttpRelationshipClient;

public class Client {
    @Getter
    private final ClientConfig config;

    @Getter
    private final HttpRelationshipClient httpRelationshipClient;

    public Client(ClientConfig config) {
        this.config = config;
        this.httpRelationshipClient = new HttpRelationshipClient(this);
    }
}
