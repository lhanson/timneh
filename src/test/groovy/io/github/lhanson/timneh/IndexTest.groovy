package io.github.lhanson.timneh

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
class IndexTest extends Specification {
	@Autowired MockMvc mvc

	def "index controller"() {
		when:
			def result = mvc
					.perform(MockMvcRequestBuilders.get("/index.html"))
					.andReturn()

		then:
			result.response.status == 200
			result.response.contentAsString.contains '<title>Timneh</title>'
	}

}
