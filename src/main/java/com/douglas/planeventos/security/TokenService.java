package com.douglas.planeventos.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class TokenService {
	
	@Value("${jwt.secret}")
	  private String secretPhrase;

	  public String generateToken(UserSS userSS) {
	    try {
	      Algorithm algorithm = Algorithm.HMAC256(secretPhrase);
	      String token = JWT.create()
	          .withIssuer("PlanEventos")
	          .withSubject(userSS.getUsername())
	          .sign(algorithm);
	      return token;
	    } catch (JWTCreationException e){
	      throw new RuntimeException("Error while generating token ", e);
	    }
	  }

	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secretPhrase);
			return JWT.require(algorithm)
					.withIssuer("PlanEventos")
					.build()
					.verify(token)
					.getSubject();
		} catch (JWTVerificationException e){
			return "";
		}
	}

	private Instant genExpirationDate(){
		return LocalDateTime.now().plusMinutes(180).toInstant(ZoneOffset.of("-03:00"));
	}
}
