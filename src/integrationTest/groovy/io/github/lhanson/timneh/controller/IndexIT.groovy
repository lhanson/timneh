package io.github.lhanson.timneh.controller

import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IndexIT extends AbstractIntegrationSpec {
	String baseUrl

	def setup() {
		baseUrl = "http://localhost:$port/"
	}

	def "get root"() {
		when:
			def response = template.getForEntity(baseUrl, String)

		then:
			response.body.contains "<title>Timneh</title>"
	}

	def "get index partial"() {
		when:
			def response = template.getForEntity("$baseUrl/index.html", String)

		then:
			response.body.contains "<title>Timneh</title>"
	}

}
