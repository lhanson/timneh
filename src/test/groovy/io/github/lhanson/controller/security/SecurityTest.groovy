package io.github.lhanson.controller.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest extends Specification {
	@Autowired MockMvc mvc

	def "Unauthenticated user cannot access authenticated endpoint"() {
		when:
			def result = mvc
					.perform(MockMvcRequestBuilders.get("/now"))
					.andReturn()

		then:
			401 == result.response.status
	}

}
