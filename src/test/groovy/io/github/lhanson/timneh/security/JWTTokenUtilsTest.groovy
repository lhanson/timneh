package io.github.lhanson.timneh.security

import io.github.lhanson.timneh.domain.UserDetails
import io.jsonwebtoken.SignatureException
import spock.lang.Specification

class JWTTokenUtilsTest extends Specification {
	JWTTokenUtils tokenUtils
	UserDetails userDetails = new UserDetails('username', 'password', [])

	def setup() {
		tokenUtils = new JWTTokenUtils()
		tokenUtils.expiration = System.currentTimeMillis() + 1000
		tokenUtils.secret = 'secret'
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
			def token = tokenUtils.generateToken(userDetails)

		when:
			def username = tokenUtils.getUsernameFromToken(token)
		then:
			username == userDetails.username
	}

	def "getExpirationDateFromToken"() {
		given:
			def token = tokenUtils.generateToken(userDetails)

		when:
			def expiration = tokenUtils.getExpirationDateFromToken(token)

		then:
			expiration
	}

	def "validateToken"() {
		when:
			def token = tokenUtils.generateToken(userDetails)
		then:
			tokenUtils.validateToken(token, userDetails)
	}

	def "Tampered tokens are not valid"() {
		given:
			def token = tokenUtils.generateToken(userDetails)

		when:
			tokenUtils.validateToken(token + 'a', userDetails)

		then:
			thrown SignatureException
	}

	def "Tokens are not valid for a different user"() {
		when:
			def token = tokenUtils.generateToken(userDetails)

		then:
			!tokenUtils.validateToken(token, new UserDetails('different_user', 'password', []))
	}

}
