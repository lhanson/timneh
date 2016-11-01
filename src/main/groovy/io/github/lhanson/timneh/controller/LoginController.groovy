package io.github.lhanson.timneh.controller

import io.github.lhanson.timneh.domain.UserDetails
import io.github.lhanson.timneh.security.JWTTokenUtils
import io.github.lhanson.timneh.security.UserDetailsService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController {
	Logger log = LoggerFactory.getLogger(this.class)
	@Autowired UserDetailsService userDetailsService
	@Autowired AuthenticationManager authenticationManager
	@Autowired JWTTokenUtils tokenUtils

	@PostMapping(value = '/login', produces = 'text/plain')
	String login(@RequestBody AuthenticationRequest authenticationRequest) {
		log.trace "Got login request for username '${authenticationRequest.username}'"
		// Perform the authentication
		Authentication authentication = this.authenticationManager.authenticate(
		  new UsernamePasswordAuthenticationToken(
				  authenticationRequest.username,
				  authenticationRequest.password))
		SecurityContextHolder.context.authentication = authentication

		// Reload password post-authentication so we can generate token
		UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username)
		return tokenUtils.generateToken(userDetails)
	}

	static class AuthenticationRequest {
		String username
		String password
	}
}
