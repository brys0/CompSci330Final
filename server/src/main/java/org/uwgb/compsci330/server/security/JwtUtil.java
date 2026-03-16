package org.uwgb.compsci330.server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.uwgb.compsci330.server.ServerConfiguration;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private final static Key key = Keys.hmacShaKeyFor(ServerConfiguration.JWT_SECRET.getBytes());
    public static String generateToken(String id) {


        return Jwts.builder()
                .subject(id)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ServerConfiguration.JWT_EXPIRATION_MS))
                .signWith(key)
                .compact();
    }

    public static String getUserIdFromToken(String token) {
        if (token.startsWith("Bearer ")) token = token.substring(7);
        Claims claims = validateToken(token);
        return claims.getSubject();
    }

    public static Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }
}
