package org.uwgb.compsci330.server.security.response;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.uwgb.compsci330.server.dto.response.ErrorResponse;
import org.uwgb.compsci330.server.exception.UnauthorizedException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response
                .getWriter()
                .write(
                    objectMapper.writeValueAsString(
                            new ErrorResponse(
                                    new UnauthorizedException().getMessage()
                            )
                    )
                );

    }
}
