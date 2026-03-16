package org.uwgb.compsci330.common.websocket.model.out.status;

import org.uwgb.compsci330.common.model.response.user.UserStatus;

public record StatusEventPayload(
        String userId,
        UserStatus status
) {}