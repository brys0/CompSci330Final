package org.uwgb.compsci330.server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.uwgb.compsci330.server.Configuration;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    public static String generateToken(String id, String username) {
        Key key = Keys.hmacShaKeyFor(Configuration.JWT_SECRET.getBytes());

        return Jwts.builder()
                .setSubject(id)
                .claim("userId", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Configuration.JWT_EXPIRATION_MS))
                .signWith(key)
                .compact();
    }

    public static String getUserIdFromToken(String token) {
        if (token.startsWith("Bearer ")) token = token.substring(7);
        Claims claims = validateToken(token);
        return claims.get("userId", String.class);
    }

    public static Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Configuration.JWT_SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }
}
