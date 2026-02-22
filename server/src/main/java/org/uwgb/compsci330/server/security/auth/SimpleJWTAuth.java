package org.uwgb.compsci330.server.security.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

// We create a dummy class to get around Spring's bs
public class SimpleJWTAuth implements Authentication {
    private final String userId;
    private boolean authenticated = true;

    public SimpleJWTAuth(String userId) { this.userId = userId; }

    @Override public String getName() { return userId; }
    @Override public Object getPrincipal() { return userId; }
    @Override public boolean isAuthenticated() { return authenticated; }
    @Override public void setAuthenticated(boolean auth) { this.authenticated = auth; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(new SimpleGrantedAuthority("USER")); }
    @Override public Object getCredentials() { return null; }
    @Override public Object getDetails() { return null; }
}
