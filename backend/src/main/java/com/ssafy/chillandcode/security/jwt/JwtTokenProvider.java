package com.ssafy.chillandcode.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpirationMs;
    
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpirationMs;
    
    //Access Token 생성
    public String createAccessToken(Long userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpirationMs);

        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // 🔥 userId만
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    // 토큰 검증 (서명, 만료, 형식)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token); // 검증
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    // 토큰에서 userId 추출
    public Long getUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }
    
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
    
    // Refresh Token 생성
    public String createRefreshToken(Long userId) {
    	Date now = new Date();
    	Date expiry = new Date(now.getTime() + refreshTokenExpirationMs);
    	
    	return Jwts.builder()
    			.setSubject(String.valueOf(userId))
    			.setIssuedAt(now)
    			.setExpiration(expiry)
    			.signWith(getSigningKey(), SignatureAlgorithm.HS256)
    			.compact();
    }
    
    // Refresh Token에서 userId 추출
    public Long getUserIdFromRefreshToken(String token) {
    	Claims claims = Jwts.parserBuilder()
    			.setSigningKey(getSigningKey())
    			.build()
    			.parseClaimsJws(token)
    			.getBody();
    	
    	return Long.valueOf(claims.getSubject());
    }
    

}

