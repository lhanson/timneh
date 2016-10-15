package io.github.lhanson.timneh.security

import io.github.lhanson.timneh.domain.UserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.web.FilterChainProxy
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import java.sql.Timestamp

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest extends Specification {
	@Autowired WebApplicationContext wac
	@Autowired FilterChainProxy springSecurityFilterChain
	MockMvc mvc

	def setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(wac)
				.addFilters(springSecurityFilterChain)
				.build()
	}

	def "Unauthenticated user cannot access authenticated endpoint"() {
		when:
			def result = mvc
					.perform(get("/now"))
					.andReturn()

		then:
			result.response.status == 401
	}

	def "Incorrect login password results in HTTP 403"() {
		when:
			def result = mvc
					.perform(post("/user")
						.header('Authorization', 'Basic ABC123_wrong hash'))
					.andReturn()

		then:
			result.response.status == 403
	}

	def "Correct login password results in successful response"() {
		given:
			UserDetails userDetails = new UserDetails('username', 'password', [])

		when:
			def result = mvc
					.perform(get("/user")
						.with(user(userDetails)))
					.andReturn()

		then:
			result.response.status == 200
	}

	def "Authenticated user can access user information"() {
		given:
			UserDetails userDetails = new UserDetails(1, 'username', 'Firstname M. Lastname', 'password', 'email@domain.tld', new Timestamp(new Date().time), [])

		when:
			def result = mvc
					.perform(get("/user")
						.with(user(userDetails)))
					.andReturn()

		then:
			result.response.contentAsString.contains 'username'
			result.response.contentAsString.contains 'Firstname M. Lastname'
			result.response.contentAsString.contains 'email@domain.tld'
			result.response.status == 200
	}

}
