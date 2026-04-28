package org.uwgb.compsci330.server.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.uwgb.compsci330.server.security.filter.JwtAuthenticationFilter;
import org.uwgb.compsci330.server.security.response.JWTAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class DefaultSecurityConfig {
    private final JwtAuthenticationFilter jwtFilter;
    private final JWTAuthenticationEntryPoint jwtEntryPoint;

    public DefaultSecurityConfig(JwtAuthenticationFilter jwtFilter, JWTAuthenticationEntryPoint jwtEntryPoint) {
        this.jwtFilter = jwtFilter;
        this.jwtEntryPoint = jwtEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtEntryPoint))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/ws").permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/health").permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/users/register").permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/users/login").permitAll())
//                .authorizeHttpRequests(auth -> auth.requestMatchers("/v3/*").permitAll())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated());

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Ignore Actuator health endpoint (bypasses all security filters)
        return web -> web.ignoring().requestMatchers("/ws");
    }
}
