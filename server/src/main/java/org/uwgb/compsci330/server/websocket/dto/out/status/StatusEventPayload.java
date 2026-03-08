package org.uwgb.compsci330.server.websocket.dto.out.status;

import org.uwgb.compsci330.server.dto.response.SafeUser;
import org.uwgb.compsci330.server.entity.user.UserStatus;

public record StatusEventPayload(
        String userId,
        UserStatus status
) {}