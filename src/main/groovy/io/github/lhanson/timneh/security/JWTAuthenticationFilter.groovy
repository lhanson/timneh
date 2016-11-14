package io.github.lhanson.timneh.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.SignatureException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	Logger log = LoggerFactory.getLogger(this.class)
	@Autowired
	private UserDetailsService userDetailsService
	@Autowired
	private JWTTokenUtils tokenUtils
	@Value('${jwt.token.header}')
	private String tokenHeader

	@Override
	void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request
		HttpServletResponse httpResponse = (HttpServletResponse) response

		String authToken = tokenUtils.readToken(httpRequest.getHeader(tokenHeader))
		if (SecurityContextHolder.context.authentication && !authToken) {
			log.debug "No auth token, authentication: ${SecurityContextHolder.context.authentication}"
			if (SecurityContextHolder.context.authentication instanceof io.github.lhanson.timneh.domain.UserDetails) {
				UserDetails userDetails = SecurityContextHolder.context.authentication.principal
				log.debug "Generating token for ${userDetails.username}"
				def token = tokenUtils.generateToken(userDetails)
				httpResponse.writer.write(token)
				return
			}
		}

		String username
		try {
			username = authToken ? tokenUtils.getUsernameFromToken(authToken) : null
			log.trace "Got token for user '$username'"
		} catch (ExpiredJwtException e) {
			logger.debug "Expired token: ${e.message}"
		} catch (SignatureException e) {
			logger.debug "Invalid token: ${e.message}"
		}

		if (username) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username)
			if (tokenUtils.validateToken(authToken, userDetails)) {
				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest))
				SecurityContextHolder.context.authentication = authentication
				log.trace "Authenticating $username"
			} else {
				log.error "Invalid token for user $username"
			}
		}

		chain.doFilter(request, response)
	}

	@Override
	@Autowired
	void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager)
	}

}
