package io.github.lhanson.timneh.security

import io.github.lhanson.timneh.user.UserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import java.sql.Timestamp

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest extends Specification {
	@Autowired JWTTokenUtils tokenUtils
	@Autowired MockMvc mvc

	def "Unauthenticated user cannot access authenticated endpoint"() {
		when:
			def result = mvc
					.perform(get("/user"))
					.andReturn()

		then:
			result.response.status == 401
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
			// The request actually hits the UserDao, and only uses the UserDetails provided for its id
			result.response.contentAsString.contains 'username'
			result.response.status == 200
	}

}
