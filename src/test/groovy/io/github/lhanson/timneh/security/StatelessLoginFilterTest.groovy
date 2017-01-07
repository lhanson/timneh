package io.github.lhanson.timneh.security

import io.github.lhanson.timneh.user.UserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import spock.lang.Specification

import javax.servlet.http.HttpServletResponse

@SpringBootTest
class StatelessLoginFilterTest extends Specification {
	@Autowired JWTTokenUtils tokenUtils
	StatelessLoginFilter statelessLoginFilter

	def setup() {
		statelessLoginFilter = new StatelessLoginFilter(
				'/login',
				Mock(AuthenticationManager),
				Mock(AuthenticationFailureHandler),
				Mock(UserDetailsService),
				tokenUtils)
	}

	def "Null password provided"() {
		given:
			def encoded = 'nonexistent_user:'.bytes.encodeBase64().toString()
			def request = new MockHttpServletRequest()
			request.addHeader("Authorization", "Basic $encoded")

		when:
			statelessLoginFilter.attemptAuthentication(request, Mock(HttpServletResponse))

		then:
			thrown BadCredentialsException
	}

}
