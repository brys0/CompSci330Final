package org.uwgb.compsci330.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.uwgb.compsci330.server.APIServerApplication;
import org.uwgb.compsci330.server.ServerConfiguration;
import org.uwgb.compsci330.server.dto.HealthResponse;

@RestController
public class BaseAPIController {
    @GetMapping("/health")
    public HealthResponse health() {
        return new HealthResponse(
                ServerConfiguration.SERVER_VERSION,
                APIServerApplication.startTime
        );
    }
}
