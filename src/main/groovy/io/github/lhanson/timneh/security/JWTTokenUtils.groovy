package io.github.lhanson.timneh.security

import io.github.lhanson.timneh.user.UserDetails
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Component
class JWTTokenUtils {
	Logger log = LoggerFactory.getLogger(this.class)
	@Value('${jwt.token.secret?:sssshhhh!}')
	String secret
	@Value('${jwt.token.expiration}')
	Long expiration
	def headerRegex = /Bearer (.*)/

	String readToken(String tokenHeader) {
		def match = (tokenHeader =~ headerRegex)
		match ? match[0][1] : null
	}

	String getUsername(Claims claims) {
		claims.getSubject()
	}

	String getFullname(Claims claims) {
		claims.get('fn', String)
	}

	Integer getUid(Claims claims) {
		claims.get('uid', Integer)
	}

	Claims getClaimsFromToken(String token) {
		Jwts.parser()
				.setSigningKey(secret)
				.parseClaimsJws(token)
				.getBody()
	}

	private Date generateCurrentDate() {
		new Date(System.currentTimeMillis())
	}

	private Date generateExpirationDate() {
		new Date(System.currentTimeMillis() + expiration * 1000)
	}

	Boolean isTokenExpired(Claims claims) {
		claims.getExpiration().before(generateCurrentDate())
	}

	String generateToken(UserDetails userDetails) {
		generateTokenForClaims([
				'sub': userDetails.username,
				'uid': userDetails.id,
				'fn' : userDetails.fullName
		])
	}

	private String generateTokenForClaims(Map<String, Object> claims) {
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact()
	}

	Boolean isValid(Claims claims) {
		!isTokenExpired(claims)
	}

	@PostConstruct
	init() {
		if (secret == 'sssshhhh!') {
			log.warn '********************************************************************************'
			log.warn '********************************************************************************'
			log.warn '**  WARNING: Timneh is running with the default secret key. This is fine for   *'
			log.warn '**     development or testing, but when storing actual user data you           *'
			log.warn '**     MUST override this with a unique secret. See the README for details.    *'
			log.warn '********************************************************************************'
			log.warn '********************************************************************************'
		}
	}
}
