package com.practice.utils;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtils {

	public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
	
	public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, userDetails);
    }
	
	public boolean IsTokenvalid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
	
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
	
	private String generateToken(Map<String, Object> claims, UserDetails userDetails) {

        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
        		.setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSigningKey() ,SignatureAlgorithm.HS256).compact();
    }
	
	private String generateRefreshToken(Map<String, Object> EXTRAclaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(EXTRAclaims).setSubject(userDetails.getUsername())
        		.setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() +604800000))
                .signWith(getSigningKey() ,SignatureAlgorithm.HS256).compact();
    }
	
	private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
	
	public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
	
	private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
        		.parseClaimsJws(token)
        		.getBody();
    }
	
	private Key getSigningKey() {
		byte [] keyBytes= Decoders.BASE64.decode("413F4428472B4B650655368566D5970337336763979244226452948404D6351");
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
