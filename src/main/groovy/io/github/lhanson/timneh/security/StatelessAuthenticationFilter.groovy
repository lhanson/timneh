package io.github.lhanson.timneh.security

import io.github.lhanson.timneh.user.UserDetails
import io.github.lhanson.timneh.user.UserAuthentication
import io.github.lhanson.timneh.user.UserDetailsService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.SignatureException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Parses the user Authentication out of the JWT token, if present,
 * and sets it in the current security context.
 */
class StatelessAuthenticationFilter extends GenericFilterBean {
    Logger log = LoggerFactory.getLogger(this.class)
    JWTTokenUtils tokenUtils
    String tokenHeader
    UserDetailsService userDetailsService
    AuthenticationFailureHandler authenticationFailureHandler

    StatelessAuthenticationFilter(String tokenHeader, JWTTokenUtils tokenUtils, UserDetailsService userDetailsService, AuthenticationFailureHandler authFailureHandler) {
        this.tokenHeader = tokenHeader
        this.tokenUtils = tokenUtils
        this.userDetailsService = userDetailsService
        this.authenticationFailureHandler = authFailureHandler
    }

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request
        HttpServletResponse httpResponse = (HttpServletResponse) response
        String token = tokenUtils.readToken(httpRequest.getHeader(tokenHeader))
        log.debug "Read token from header: $token"
        if (token) {
            try {
                Claims claims = tokenUtils.getClaimsFromToken(token)
                log.debug "Examining token for username ${claims.getSubject()}"
                if (tokenUtils.isValid(claims)) {
                    log.trace "Valid token for ${claims.getSubject()}"
                    // Derive UserDetails from token rather than doing a database lookup
                    def user = new UserDetails(
                            tokenUtils.getUid(claims),
                            tokenUtils.getUsername(claims),
                            tokenUtils.getFullname(claims),
                            '[Not stored in JWT token]',     // password
                            '[Not stored in JWT token]',     // email
                            null,                            // created
                            []                               // granted authorities
                    )
                    SecurityContextHolder.context.authentication =
                            new UserAuthentication(user: user)
                }
            } catch (ExpiredJwtException | SignatureException  e) {
                AuthenticationException ae = new AuthenticationException(e.message, e) { }
                authenticationFailureHandler.onAuthenticationFailure(httpRequest, httpResponse, ae)
                println "returning"
                return // return 401, don't continue filter chain
            }
        }
        chain.doFilter(request, response)
    }

}
