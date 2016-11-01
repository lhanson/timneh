package io.github.lhanson.timneh.controller

import io.github.lhanson.timneh.security.JWTTokenUtils
import io.github.lhanson.timneh.security.UserDetailsService
import org.springframework.security.authentication.AuthenticationManager
import spock.lang.Specification

class LoginControllerTest extends Specification {
	LoginController controller
	LoginController.AuthenticationRequest authRequest = new LoginController.AuthenticationRequest()

	def setup() {
		controller = new LoginController()
		controller.authenticationManager = Mock(AuthenticationManager)
		controller.userDetailsService = Mock(UserDetailsService)
	}

	def "Login returns a token and 200 status"() {
		given:
			controller.tokenUtils = Mock(JWTTokenUtils)
			controller.tokenUtils.generateToken(_) >> 'jwt_token'

		when:
			String token= controller.login(authRequest)

		then:
			token == 'jwt_token'
	}
}
