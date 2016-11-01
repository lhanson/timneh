package io.github.lhanson.timneh.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
class JWTTokenUtils {
	Logger log = LoggerFactory.getLogger(this.class)
	@Value('${jwt.token.secret}')
	String secret
	@Value('${jwt.token.expiration}')
	Long expiration
	def headerRegex = /Bearer (.*)/

	String readToken(String tokenHeader) {
		def match = (tokenHeader =~ headerRegex)
		match ? match[0][1] : null
	}

	String getUsernameFromToken(String token) {
		getClaimsFromToken(token)?.getSubject()
	}

	Date getExpirationDateFromToken(String token) {
		getClaimsFromToken(token)?.getExpiration()
	}

	private Claims getClaimsFromToken(String token) {
		try {
			Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody()
		} catch (Exception e) {
			log.error "Error getting claims from token", e
			throw e
		}
	}

	private Date generateCurrentDate() {
		new Date(System.currentTimeMillis())
	}

	private Date generateExpirationDate() {
		new Date(System.currentTimeMillis() + expiration * 1000)
	}

	private Boolean isTokenExpired(String token) {
		Date expiration = getExpirationDateFromToken(token)
		return expiration.before(generateCurrentDate())
	}

	String generateToken(UserDetails userDetails) {
		generateTokenForClaims(['sub': userDetails.username])
	}

	private String generateTokenForClaims(Map<String, Object> claims) {
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact()
	}

	Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token)
		return (username == userDetails.username && !isTokenExpired(token))
	}
}
