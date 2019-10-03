package com.fitcrew.FitCrewAppTrainings.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.util.Date;

public class TokenJwtUtil {

	private static final String AUTHORITIES_KEY = "roles";
	private static final String FULL_NAME = "fullName";
	private static final String USER_ID = "userId";

	private TokenJwtUtil() {

	}

	public static String createToken(String userName, Integer userId, String role) {

		long now = (new Date()).getTime();
		Date validity = new Date(now + 43200 * 1000);

		return Jwts.builder()
				.setSubject(userName)
				.claim(USER_ID, userId)
				.claim(AUTHORITIES_KEY, role)
				.claim(FULL_NAME, userName + " " + userName)
				.signWith(SignatureAlgorithm.HS512, MacProvider.generateKey(SignatureAlgorithm.HS256))
				.setExpiration(validity)
				.compact();
	}
}
