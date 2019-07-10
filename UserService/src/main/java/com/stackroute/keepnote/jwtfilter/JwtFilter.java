package com.stackroute.keepnote.jwtfilter;

import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

/* This class implements the custom filter by extending org.springframework.web.filter.GenericFilterBean.  
 * Override the doFilter method with ServletRequest, ServletResponse and FilterChain.
 * This is used to authorize the API access for the application.
 */

public class JwtFilter extends GenericFilterBean {

	private static final String HDR_AUTH = "Authorization";

	private final String secret;

	public JwtFilter(String secret) {
		this.secret = secret;
	}

	/*
	 * Override the doFilter method of GenericFilterBean. Retrieve the
	 * "authorization" header from the HttpServletRequest object. Retrieve the
	 * "Bearer" token from "authorization" header. If authorization header is
	 * invalid, throw Exception with message. Parse the JWT token and get claims
	 * from the token using the secret key Set the request attribute with the
	 * retrieved claims Call FilterChain object's doFilter() method
	 */

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String authHeader = req.getHeader(HDR_AUTH);
		if (null == authHeader || !authHeader.startsWith("Bearer ")) {
			throw new ServletException("missing bearer token");
		}

		String compactJWT = authHeader.substring(7);
		try {
			Claims token = Jwts.parser().setSigningKey(secret).parseClaimsJwt(compactJWT).getBody();
			request.setAttribute("token", token);
		} catch (Exception e) {
			throw new ServletException(e.getMessage());
		}
		chain.doFilter(request, response);
	}
}
