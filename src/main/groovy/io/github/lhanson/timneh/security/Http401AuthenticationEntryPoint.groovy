package io.github.lhanson.timneh.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class Http401AuthenticationEntryPoint implements AuthenticationEntryPoint {
	Logger log = LoggerFactory.getLogger(this.class)

	@Override
	void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		log.debug "Commencing authentication scheme"
		log.debug "Response headers: ${response.getHeaderNames()}"
		// Required per RFC 2616 10.4.2
		response.addHeader('WWW-Authenticate', 'Bearer token_type="JWT"')
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied")
	}
}
