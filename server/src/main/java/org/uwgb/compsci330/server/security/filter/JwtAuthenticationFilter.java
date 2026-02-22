package org.uwgb.compsci330.server.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.uwgb.compsci330.server.security.JwtUtil;
import org.uwgb.compsci330.server.security.auth.SimpleJWTAuth;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        try {
            if (authHeader != null && !authHeader.isBlank()) {
                String userId = JwtUtil.getUserIdFromToken(authHeader);

                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority(("ROLE_USER")))));
            }
        } catch (Exception e) {
            logger.warn("Continuing without authentication: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

}
