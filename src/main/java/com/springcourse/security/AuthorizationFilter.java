package com.springcourse.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcourse.constant.SecurityConstants;
import com.springcourse.resource.exception.ApiError;

import io.jsonwebtoken.Claims;

public class AuthorizationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtManager jwtManager;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
				
		if (jwt == null || !jwt.startsWith(SecurityConstants.JWT_PROVIDER)) {
			responseError(response, SecurityConstants.JWT_INVALID_MSG);
			return;
		}
		
		jwt = jwt.replace(SecurityConstants.JWT_PROVIDER, "");
		
		try {
			Claims claims = jwtManager.parseToken(jwt);
			String email = claims.getSubject();
			List<String> roles = (List<String>) claims.get(SecurityConstants.JWT_ROLE_KEY);
			
			List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
			roles.forEach(role -> {
				grantedAuthorities.add(new SimpleGrantedAuthority(role));
			});
			
			Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, grantedAuthorities);
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		} catch (Exception e) {
			responseError(response, e.getMessage());
			return;
		}
		
		
	}
	
	private void responseError(HttpServletResponse response, String message) throws IOException {
		ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(), message, new Date());
		ObjectMapper objectMapper = new ObjectMapper();
		String apiErrorString = objectMapper.writeValueAsString(apiError);
		
		PrintWriter writer = response.getWriter();
		writer.write(apiErrorString);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}

}
