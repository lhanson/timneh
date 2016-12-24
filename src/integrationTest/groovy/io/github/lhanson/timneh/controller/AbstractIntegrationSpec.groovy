package io.github.lhanson.timneh.controller

import io.github.lhanson.timneh.domain.UserDetails
import io.github.lhanson.timneh.security.JWTTokenUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AbstractIntegrationSpec extends Specification {
	@Autowired TestRestTemplate template
	@Autowired JWTTokenUtils tokenUtils
	@LocalServerPort int port
	String baseUrl
	HttpEntity<?> authenticatedRequest
	HttpHeaders headersWithToken
	Long originalExpiration

	def setup() {
		// Create an HttpEntity request with a correct JWT token
		def user = new UserDetails("user", "password", [])
		user.id = 1
		String token = tokenUtils.generateToken(user)
		headersWithToken = new HttpHeaders()
		headersWithToken.set("Authorization", "Bearer $token")
		authenticatedRequest = new HttpEntity<>(headersWithToken)
		originalExpiration = tokenUtils.expiration
	}

	def cleanup() {
		// Avoid any side effects caused by tests which tamper with expiration
		tokenUtils.expiration = originalExpiration
	}
}
