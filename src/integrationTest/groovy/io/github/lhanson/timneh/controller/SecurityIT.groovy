package io.github.lhanson.timneh.controller

import io.github.lhanson.timneh.domain.UserDetails
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders

import static org.springframework.http.HttpMethod.GET

class SecurityIT extends AbstractIntegrationSpec {

	def setup() {
		baseUrl = "http://localhost:$port"
	}

	def "Presenting valid credentials results in a correct token"() {
		given:
			def authTemplate = template.withBasicAuth('user', 'password')

		when:
			def response = authTemplate.postForEntity("$baseUrl/login", null, String)

		then:
			response.statusCodeValue == 200
			response.body.count('.') == 2 // proper JWT token
	}

	def "Presenting an invalid password results in a 401"() {
		given:
			def authTemplate = template.withBasicAuth('user', 'WRONGpassword')

		when:
			def response = authTemplate.postForEntity("${baseUrl}/login", null, String)

		then:
			response.statusCodeValue == 401
			// Required per RFC2616 (10.4.2)
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
			def response = template.exchange(baseUrl, GET, request, String)

		then:
			response.statusCodeValue == 401
			// Required per RFC2616 (10.4.2)
			response.headers.get('WWW-Authenticate')
	}

	def "Presenting a tampered token results in 401"() {
		given:
			String token = tokenUtils.generateToken(new UserDetails("user", "password", []))
			HttpHeaders headersWithToken = new HttpHeaders()
			headersWithToken.set("Authorization", "Bearer ${token + 'a'}")
			HttpEntity request = new HttpEntity<>(headersWithToken)

		when:
			def response = template.exchange(baseUrl, GET, request, String)

		then:
			response.statusCodeValue == 401
			// Required per RFC2616 (10.4.2)
			response.headers.get('WWW-Authenticate')
	}

}
