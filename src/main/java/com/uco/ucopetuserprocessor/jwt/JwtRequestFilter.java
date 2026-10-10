package com.uco.ucopetuserprocessor.jwt;

public class JwtRequestFilter {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String BEARER_TOKEN_PREFIX = "Bearer ";

	public boolean hasBearerToken(String authorizationHeader) {
		return authorizationHeader != null && authorizationHeader.startsWith(BEARER_TOKEN_PREFIX);
	}
}