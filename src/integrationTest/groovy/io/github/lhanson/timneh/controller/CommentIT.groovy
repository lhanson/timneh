package io.github.lhanson.timneh.controller

import io.github.lhanson.timneh.domain.Comment
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity

import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.POST

class CommentIT extends AbstractIntegrationSpec {

	def setup() {
		baseUrl = "http://localhost:$port/comments"
	}

	def "create comment"() {
		given:
			HttpEntity request = new HttpEntity("Integration Test Comment", headersWithToken)
			def discussionId = 1

		when:
			ResponseEntity response = template.exchange(
					baseUrl + "/$discussionId", POST, request, ResponseEntity)

		then:
			response.statusCodeValue == 201
			// Assume comment ID 2 since 1 is auto-populated with test data
			response.headers['Location'][0] == "$baseUrl/$discussionId/2"
	}

	def "get comments by discussion ID"() {
		when:
			def response = template.exchange(
					baseUrl + '/1', GET, authenticatedRequest, List)

		then:
			response.statusCodeValue == 200
			response.body instanceof List
	}

	def "get comments by invalid discussion ID returns 404"() {
		when:
			def response = template.exchange(
					baseUrl + '/9999', GET, authenticatedRequest, Comment)

		then:
			response.statusCodeValue == 404
	}

}
