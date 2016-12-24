package io.github.lhanson.timneh.security

import io.github.lhanson.timneh.user.UserDetails
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.SignatureException
import spock.lang.Specification

class JWTTokenUtilsTest extends Specification {
	JWTTokenUtils tokenUtils
	UserDetails userDetails = new UserDetails('username', 'password', [])
	String token

	def setup() {
		tokenUtils = new JWTTokenUtils()
		tokenUtils.expiration = System.currentTimeMillis() + 1000
		tokenUtils.secret = 'secret'
		token = tokenUtils.generateToken(userDetails)
	}

	def "Tokens are correctly parsed when prefixed with 'Bearer' scheme"() {
		given:
			def tokenHeader = 'Bearer TOKEN_TEXT'

		when:
			def token = tokenUtils.readToken(tokenHeader)

		then:
			token == 'TOKEN_TEXT'
	}

	def "getUsernameFromToken"() {
		given:
			Claims claims = tokenUtils.getClaimsFromToken(token)

		when:
			def username = tokenUtils.getUsername(claims)

		then:
			username == userDetails.username
	}

	def "validateToken"() {
		when:
			Claims claims = tokenUtils.getClaimsFromToken(token)

		then:
			tokenUtils.isValid(claims)
	}

	def "Tampered tokens are not valid"() {
		when:
			tokenUtils.getClaimsFromToken(token + 'a')

		then:
			thrown SignatureException
	}

	def "Expired tokens are not valid"() {
		given:
			tokenUtils.expiration = -1
			token = tokenUtils.generateToken(userDetails)

		when:
			tokenUtils.getClaimsFromToken(token)

		then:
			thrown ExpiredJwtException
	}

}
