package ru.kpfu.itis.amirova.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.amirova.model.User;

import javax.crypto.SecretKey;
import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

//@Component
public class JwtProvider {
    @Value("${jwt.access.secret}")
    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;

    public JwtProvider(@Value("${jwt.access.access") String jwtAccessSecret,
                       @Value("${jwt.refresh.access") String jwtRefreshSecret) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }

    public String generateAccessToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        Instant accessExpirationTime = now.plusHours(1).atZone(ZoneId.systemDefault()).toInstant();

        return Jwts.builder()
                .subject(user.getUsername())
                .expiration(Date.from(accessExpirationTime))
                .signWith(jwtAccessSecret)
                .claim("roles", user.getRoles())
                .claim("email", user.getEmail())
                .compact();
    }

    public String generateRefreshToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        Instant refreshExpirationTime = now.plusDays(1).atZone(ZoneId.systemDefault()).toInstant();
        return Jwts.builder()
                .subject(user.getUsername())
                .expiration(Date.from(refreshExpirationTime))
                .signWith(jwtRefreshSecret)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, jwtAccessSecret);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, jwtAccessSecret);
    }

    public Claims getAccessClaims(String token) {
        return getClaimsFromToken(token, jwtAccessSecret);
    }

    public Claims getRefreshClaims(String token) {
        return getClaimsFromToken(token, jwtRefreshSecret);
    }

    private Claims getClaimsFromToken(String token, SecretKey secretKey) {
        return Jwts.parser()
                .decryptWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean validateToken(String token, SecretKey secretKey) {
        return Jwts.parser()
                .decryptWith(secretKey)
                .build().isSigned(token);
    }
}
