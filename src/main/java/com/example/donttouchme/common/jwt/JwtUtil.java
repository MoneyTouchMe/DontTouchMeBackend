package com.example.donttouchme.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Configuration
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessTime;
    private final Long refreshTime;
    public JwtUtil(
            @Value("${spring.jwt.secret}") String secret,
            @Value("${spring.jwt.access.expireTime}") Long accessTime,
            @Value("${spring.jwt.refresh.expireTime}") Long refreshTime
    ) {
        this.refreshTime = refreshTime;
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessTime = accessTime;
    }

    private Claims getPayload(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public String getMemberId(String token){
        return getPayload(token).get("id", String.class);
    }

    public String getRole(String token){
        return getPayload(token).get("role", String.class);
    }

    public String getCategory(String token){
        return getPayload(token).get("category", String.class);
    }

    public Boolean isExpired(String token){
        return getPayload(token).getExpiration().before(new Date());
    }

    public String createAccessToken(Long MemberId, String role){
        return Jwts.builder()
                .claim("category", "access")
                .claim("id", MemberId)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTime))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(Long MemberId, String role){
        return Jwts.builder()
                .claim("category", "refresh")
                .claim("id", MemberId)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTime))
                .signWith(secretKey)
                .compact();
    }
}
