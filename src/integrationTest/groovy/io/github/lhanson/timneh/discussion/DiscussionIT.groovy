package io.github.lhanson.timneh.discussion

import io.github.lhanson.timneh.AbstractIntegrationSpec
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity

import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.POST

class DiscussionIT extends AbstractIntegrationSpec {

	def setup() {
		baseUrl = "http://localhost:$port/discussions"
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

	def "get discussion by invalid ID returns 404"() {
		when:
			def response = template.exchange(
					baseUrl + '/9999', GET, authenticatedRequest, Discussion)

		then:
			response.statusCodeValue == 404
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
			// Assume comment ID 2 since 1 is auto-populated with test data
			response.headers['Location'][0] == "$baseUrl/2"
	}

	def "new discussion has a created date set"() {
		given:
			HttpEntity request = new HttpEntity("New Discussion for Testing Creation Date", headersWithToken)
			template.exchange(baseUrl, POST, request, ResponseEntity)

		when:
			def response = template.exchange(
					baseUrl, GET, authenticatedRequest, List)

		then:
			response.statusCodeValue == 200
			response.body.every { it.created }
	}

}
