package com.ey.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

   
    private final String jwtSecret = "ThisIsASecretKeyThatIsAtLeast32CharsLong";
    private final long jwtExpirationMs = 86400000; // 1 day

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

   
    public String generateToken(String username, java.util.Collection<? extends GrantedAuthority> authorities) {
        List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).toList();
        Date now = new Date();
        Date expiry = new Date(System.currentTimeMillis() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .addClaims(Map.of("roles", roles))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

  
    public String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(System.currentTimeMillis() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Object roles = getAllClaims(token).get("roles");
        if (roles instanceof List<?> list) {
            return list.stream().map(Object::toString).toList();
        }
        return List.of();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    
    public boolean isTokenValidForUser(String token, String expectedUsername) {
        return validateToken(token)
                && expectedUsername != null
                && expectedUsername.equalsIgnoreCase(extractUsername(token))
                && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date exp = extractExpiration(token);
        return exp != null && exp.before(new Date());
    }

    public Date extractExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public long getExpirationMillis() {
        return jwtExpirationMs;
    }

   
    private <T> T getClaim(String token, Function<Claims, T> resolver) {
        Claims claims = getAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
