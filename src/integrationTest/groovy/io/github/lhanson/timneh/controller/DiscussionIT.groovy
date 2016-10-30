package io.github.lhanson.timneh.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DiscussionIT extends Specification {
	@Autowired TestRestTemplate template
	@LocalServerPort int port
	String baseUrl

	def setup() {
		baseUrl = "http://localhost:$port/discussions"
		template = template.withBasicAuth('user', 'password')
	}

	def "get discussions"() {
		when:
			def response = template.getForEntity(baseUrl, String)

		then:
			response.statusCodeValue == 200
	}

	def "get discussion by ID"() {
		when:
			def response = template.getForEntity("$baseUrl/1", String)

		then:
			response.statusCodeValue == 200
	}

	def "create discussion"() {
		given:
			HttpEntity request = new HttpEntity(
					"Integration Test Discussion",
					new LinkedMultiValueMap<String, String>(['Content-Type': ['application/json']]))

		when:
			ResponseEntity response = template.postForEntity(baseUrl, request, null)

		then:
			response.statusCodeValue == 201
			response.getHeaders().get('Location')
	}

}
