package io.github.lhanson.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IndexIT extends Specification {
	@Autowired TestRestTemplate template
	@LocalServerPort int port
	URL base

	def setup() {
		base = new URL("http://localhost:$port/")
	}

	def "get index"() {
		when:
			def response = template.getForEntity(base.toString(), String);

		then:
			'Greetings from Spring Boot!' == response.body
	}
}
