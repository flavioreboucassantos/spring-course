package com.springcourse.security;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;

import com.springcourse.constant.SecurityConstants;
import com.springcourse.dto.UserLoginResponseDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtManager {
	public static UserLoginResponseDTO createToken(String email, List<String> roles) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, SecurityConstants.JWT_EXP_DAYS);

		String jwt = Jwts.builder()
				.setSubject(email)
				.setExpiration(calendar.getTime())
				.claim(SecurityConstants.JWT_ROLE_KEY, roles)
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.API_KEY.getBytes())
				.compact();

		Long expireIn = calendar.getTimeInMillis();

		return new UserLoginResponseDTO(jwt, expireIn, SecurityConstants.JWT_PROVIDER);
	}

	public static Claims parseToken(String jwt) throws JwtException {
		Claims claims = Jwts.parser()
				.setSigningKey(SecurityConstants.API_KEY.getBytes())
				.parseClaimsJws(jwt)
				.getBody();

		return claims;
	}
}
