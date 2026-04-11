package org.uwgb.compsci330.client_sdk.entity.user;

import lombok.Getter;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.entity.Entity;
import org.uwgb.compsci330.client_sdk.entity.IdentifiableEntity;
import org.uwgb.compsci330.common.model.response.user.SafeUser;
import org.uwgb.compsci330.common.model.response.user.UserStatus;

public class SelfUser implements IdentifiableEntity {
    @Getter
    private final Client client;
    private final SafeUser data;

    public SelfUser(Client client, SafeUser user) {
        this.client = client;
        this.data = user;
    }

    public String getId() {
        return this.data.getId();
    }
    public String getUsername() {
        return this.data.getUsername();
    }

    public UserStatus getStatus() {
        return this.data.getStatus();
    }
}
