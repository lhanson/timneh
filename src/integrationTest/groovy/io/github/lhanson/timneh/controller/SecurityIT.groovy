package io.github.lhanson.timneh.controller

import io.github.lhanson.timneh.domain.UserDetails
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

import static org.springframework.http.HttpMethod.GET

class SecurityIT extends AbstractIntegrationSpec {

	def setup() {
		baseUrl = "http://localhost:$port"
	}

	def "Presenting valid credentials results in a correct token"() {
		given:
			def credentials = '{ "username": "user", "password": "password" }'
			HttpHeaders headers = new HttpHeaders()
			headers.setContentType(MediaType.APPLICATION_JSON)
			HttpEntity<String> requestEntity = new HttpEntity<String>(credentials, headers)

		when:
			def response = template.postForEntity("$baseUrl/login", requestEntity, String)

		then:
			response.statusCodeValue == 200
	}

	def "Presenting an invalid password results in a 401"() {
		given:
			def credentials = '{ "username": "user", "password": "WRONGpassword" }'
			HttpHeaders headers = new HttpHeaders()
			headers.setContentType(MediaType.APPLICATION_JSON)
			headers.setAccept([MediaType.ALL])
			HttpEntity<String> requestEntity = new HttpEntity<String>(credentials, headers)

		when:
			def response = template.postForEntity("${baseUrl}/login", requestEntity, String)

		then:
			response.statusCodeValue == 401
			response.headers.get('WWW-Authenticate')
	}

	def "Presenting an expired token results in a 401"() {
		given:
			// Create an HttpEntity request with an expired JWT token
			tokenUtils.expiration = -1
			String token = tokenUtils.generateToken(new UserDetails("user", "password", []))
			HttpHeaders headersWithToken = new HttpHeaders()
			headersWithToken.set("Authorization", "Bearer $token")
			HttpEntity request = new HttpEntity<>(headersWithToken)

		when:
			def response = template.exchange("$baseUrl/discussions", GET, request, String)

		then:
			response.statusCodeValue == 401
			response.getHeaders().get('WWW-Authenticate').contains'Bearer token_type="JWT"'
	}

	def "Presenting a tampered token results in 401"() {
		given:
			String token = tokenUtils.generateToken(new UserDetails("user", "password", []))
			HttpHeaders headersWithToken = new HttpHeaders()
			headersWithToken.set("Authorization", "Bearer ${token + 'a'}")
			HttpEntity request = new HttpEntity<>(headersWithToken)

		when:
			def response = template.exchange("$baseUrl/discussions", GET, request, String)

		then:
			response.statusCodeValue == 401
			response.getHeaders().get('WWW-Authenticate').contains'Bearer token_type="JWT"'
	}

}
