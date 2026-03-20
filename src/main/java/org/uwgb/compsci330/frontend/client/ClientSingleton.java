package org.uwgb.compsci330.frontend.client;

import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.ClientConfig;

public class ClientSingleton {
    private static ClientSingleton INSTANCE;

    private final Client client;

    private ClientSingleton() {
        this.client = new Client(
                new ClientConfig(
                        "http://localhost:8080" // TODO: Configure page to let user set the server to use.
                )
        );
    }

    public static ClientSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClientSingleton();
        }

        return INSTANCE;
    }

    public Client getClient() {
        return this.client;
    }
}
