package io.github.lhanson.timneh.controller

import io.github.lhanson.timneh.domain.Discussion
import io.github.lhanson.timneh.domain.UserDetails
import io.github.lhanson.timneh.security.JWTTokenUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import spock.lang.Specification

import static org.springframework.http.HttpMethod.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DiscussionIT extends Specification {
	@Autowired TestRestTemplate template
	@Autowired JWTTokenUtils tokenUtils
	@LocalServerPort int port
	String baseUrl
	HttpEntity<?> authenticatedRequest
	HttpHeaders headersWithToken

	def setup() {
		baseUrl = "http://localhost:$port/discussions"

		// Create an HttpEntity request with a correct JWT token
		String token = tokenUtils.generateToken(new UserDetails("user", "password", []))
		headersWithToken = new HttpHeaders()
		headersWithToken.set("Authorization", "Bearer $token")
		authenticatedRequest = new HttpEntity<>(headersWithToken)
	}

	def "get discussions"() {
		when:
			ResponseEntity<String> response = template.exchange(
					baseUrl, GET, authenticatedRequest, List)

		then:
			response.statusCodeValue == 200
	}

	def "get discussion by ID"() {
		when:
			def response = template.exchange(
					baseUrl + '/1', GET, authenticatedRequest, Discussion)

		then:
			response.body instanceof Discussion
			response.statusCodeValue == 200
	}

	def "create discussion"() {
		given:
			HttpEntity request = new HttpEntity("Integration Test Discussion", headersWithToken)

		when:
			ResponseEntity response = template.exchange(
					baseUrl, POST, request, ResponseEntity)

		then:
			response.statusCodeValue == 201
			response.getHeaders().get('Location')
	}

}
