package io.github.lhanson.timneh.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class Http401Handler implements AuthenticationEntryPoint, AuthenticationFailureHandler {
	Logger log = LoggerFactory.getLogger(this.class)

	@Override
	void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
		send401(response, e)
	}

	@Override
	void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
		send401(response, e)
	}

	void send401(HttpServletResponse response, AuthenticationException e) {
		log.info "Authentication error, returning 401. Cause: ${e.message}"
		response.addHeader('WWW-Authenticate', 'Bearer token_type="JWT"')
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied")
	}
}
