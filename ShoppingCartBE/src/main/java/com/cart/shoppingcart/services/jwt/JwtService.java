package com.cart.shoppingcart.services.jwt;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
     private static final String SECRET_KEY = "476744F687SAD5689B86879U0T87W788999M77S6S43577F6";


     
    public static String getToken(UserDetails userDetails, Long userId) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return getToken(claims, userDetails);
    }

    private static String getToken(Map<String,Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
            .subject(userDetails.getUsername())
            .claims(extraClaims)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
            .signWith(getKey()) // 24 hours
            .compact();
    }

    private static SecretKey getKey() {
       byte[] keyBytes =  Decoders.BASE64.decode(SECRET_KEY);
       return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts
            .parser()
            .verifyWith(getKey())
            .build().parseSignedClaims(token)
            .getPayload();
    }

    private boolean isTokenExpired(String token) {
      return getExporationDateFromToken(token).before(new Date());
    }

    public <T> T getClaims(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Date getExporationDateFromToken(String token) {
        return getClaims(token, Claims::getExpiration);
    }
}
