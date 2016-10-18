package io.github.lhanson.timneh.controller

import io.github.lhanson.timneh.domain.Discussion
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DiscussionIT extends Specification {
	@Autowired TestRestTemplate template
	@LocalServerPort int port
	String baseUrl

	def setup() {
		baseUrl = "http://localhost:$port/discussions"
	}

	def "get discussions"() {
		when:
			def response = template
					.withBasicAuth('test_username', 'test_password')
					.getForEntity(baseUrl, String)

		then:
			response.statusCodeValue == 200
	}

	def "get discussion by ID"() {
		when:
			def response = template
					.withBasicAuth('test_username', 'test_password')
					.getForEntity("$baseUrl/1", String)

		then:
			response.statusCodeValue == 200
	}

	/* TODO: Research using POST with Spring Security
	def "create discussion"() {
		given:
			Discussion newDiscussion = new Discussion(title: 'New Discussion')
		when:
			def response = template
					.withBasicAuth('test_username', 'test_password')
					.postForEntity(baseUrl, newDiscussion, Discussion, [:])
			println "Got response $response"

		then:
			false
	}
	*/

}
