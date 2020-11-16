package com.Thomas.JwtAuthCrud.security.jwt;



import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;


@Service
public class JwtUtil {

	@Value("${secret_key}")
	private String SECRET_KEY;
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims)
							 .setSubject(subject)
							 .setIssuedAt(new Date(System.currentTimeMillis()))
							 .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
							 .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();

	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		
		return createToken(claims, userDetails.getUsername());
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

}
