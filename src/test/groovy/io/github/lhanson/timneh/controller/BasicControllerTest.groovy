package io.github.lhanson.timneh.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@SpringBootTest
@AutoConfigureMockMvc
class BasicControllerTest extends Specification {
	@Autowired MockMvc mvc

	def "index partial mapping"() {
		when:
			def result = mvc
					.perform(get("/index.html"))
					.andReturn()

		then:
			result.response.status == 200
			result.response.contentAsString.contains '<title>Timneh</title>'
	}

}
