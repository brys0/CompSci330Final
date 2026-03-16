package org.uwgb.compsci330.server.mapper;

import org.uwgb.compsci330.common.model.response.user.SafeUser;
import org.uwgb.compsci330.server.entity.user.User;

public class UserMapper {
    public static SafeUser toSafe(User user) {
        return new SafeUser(
                user.getId(),
                user.getUsername(),
                user.getStatus()
        );
    }
}
