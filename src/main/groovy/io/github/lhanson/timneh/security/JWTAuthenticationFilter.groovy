package io.github.lhanson.timneh.security

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

@Component
class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	@Autowired
	private UserDetailsService userDetailsService
	@Autowired
	private JWTTokenUtils tokenUtils
	@Value('${jwt.token.header}')
	private String tokenHeader

	@Override
	void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request
		String authToken = tokenUtils.readToken(httpRequest.getHeader(tokenHeader))
		String username = authToken ? tokenUtils.getUsernameFromToken(authToken) : null

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username)
			if (tokenUtils.validateToken(authToken, userDetails)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest))
				SecurityContextHolder.getContext().setAuthentication(authentication)
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
