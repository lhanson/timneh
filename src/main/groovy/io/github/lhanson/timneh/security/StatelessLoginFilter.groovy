package io.github.lhanson.timneh.security

import io.github.lhanson.timneh.user.UserDetails
import io.github.lhanson.timneh.user.UserDetailsService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.AuthenticationFailureHandler

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Adds the token to the request upon successful authentication
 */
class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {
    Logger log = LoggerFactory.getLogger(this.class)
    AuthenticationManager authenticationManager
    AuthenticationFailureHandler authenticationFailureHandler
    UserDetailsService userDetailsService
    JWTTokenUtils tokenUtils

    protected StatelessLoginFilter(String defaultFilterProcessesUrl,
                                   AuthenticationManager authManager,
                                   AuthenticationFailureHandler authFailureHandler,
                                   UserDetailsService userDetailsService,
                                   JWTTokenUtils tokenUtils) {
        super(defaultFilterProcessesUrl)
        this.authenticationManager = authManager
        this.authenticationFailureHandler = authFailureHandler
        this.userDetailsService = userDetailsService
        this.tokenUtils = tokenUtils
    }

    @Override
    Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        log.debug "Attempting authentication"
        AuthenticationRequest authRequest = getAuthRequest(request)
        if (authRequest) {
            UserDetails user = userDetailsService.loadUserByUsername(authRequest.username)
            if (!user || authRequest.password != user.password) {
                throw new BadCredentialsException('Bad credentials')
            }
            log.debug "Loaded userDetails {}", user
            UsernamePasswordAuthenticationToken loginToken =
                    new UsernamePasswordAuthenticationToken(user.username, user.password)
            return authenticationManager.authenticate(loginToken)
        } else {
            log.debug "No authentication header found in request"
            return null
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        def token = tokenUtils.generateToken(authResult.principal)
        log.trace "Successful login for: {}, writing token {}", authResult.principal, token
        response.writer.write(token)
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        authenticationFailureHandler.onAuthenticationFailure(request, response, failed)
    }


    /** Parse the HTTP Basic auth request and return a request object */
    private AuthenticationRequest getAuthRequest(HttpServletRequest request) {
        AuthenticationRequest authenticationRequest
        def authHeaderBytes = request.getHeader('Authorization')?.
                substring('Basic '.length())?.
                decodeBase64()
        if (authHeaderBytes) {
            String authHeader = new String(authHeaderBytes)
            if (authHeader?.contains(':')) {
                def tokens = authHeader.tokenize(':')
                authenticationRequest = new AuthenticationRequest(username: tokens[0], password: tokens[1])
            }
        }
        return authenticationRequest
    }

    static class AuthenticationRequest {
        String username
        String password
    }

}
