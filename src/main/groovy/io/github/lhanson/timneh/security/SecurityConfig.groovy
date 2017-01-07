package io.github.lhanson.timneh.security

import io.github.lhanson.timneh.user.UserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired AuthenticationEntryPoint authenticationEntryPoint
	@Autowired AuthenticationManager authenticationManager
	@Autowired AuthenticationFailureHandler authenticationFailureHandler
	@Autowired UserDetailsService userDetailsService
    @Autowired JWTTokenUtils tokenUtils
	@Value('${jwt.token.header}') String tokenHeader

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.csrf()
					.disable()
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					.and()
				.authorizeRequests()
					.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
					.antMatchers(HttpMethod.GET,
							'/', '/index.html', '/home.html', '/loginForm.html',
							'/mappings', '/v2/api-docs', '/swagger-ui.html', '/swagger-resources/**').permitAll()
				    .antMatchers(HttpMethod.POST, '/login', '/index.html', '/home.html').permitAll()
					.anyRequest().authenticated()
					.and()

				 // JWT authentication
				.addFilterBefore(new StatelessLoginFilter('/login', authenticationManager, authenticationFailureHandler, userDetailsService, tokenUtils),
					UsernamePasswordAuthenticationFilter)
				.addFilterBefore(new StatelessAuthenticationFilter(tokenHeader, tokenUtils, userDetailsService, authenticationFailureHandler),
					UsernamePasswordAuthenticationFilter)

				.exceptionHandling()
					.authenticationEntryPoint(authenticationEntryPoint)
	}

	@Bean
	@Profile(['default', 'local', 'travis-ci'])
	PasswordEncoder noopPasswordEncoder() {
		new NoOpPasswordEncoder()
	}

	@Bean
	@Profile('dev')
	PasswordEncoder bcryptPasswordEncoder() {
		new BCryptPasswordEncoder()
	}

}
