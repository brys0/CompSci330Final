package org.uwgb.compsci330.server.dto;

import org.uwgb.compsci330.server.ServerConfiguration;

public record HealthResponse(
        String version,
        String onSince
) {
}
