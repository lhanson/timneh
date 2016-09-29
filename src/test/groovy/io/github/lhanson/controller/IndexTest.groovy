package io.github.lhanson.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

import static org.hamcrest.Matchers.equalTo
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class IndexTest extends Specification {
	@Autowired MockMvc mvc

	def "index controller"() {
		when:
			def response = mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))

		then:
			response.andExpect(status().isOk())
			        .andExpect(content().string(equalTo('Greetings from Spring Boot!')));
	}

}
