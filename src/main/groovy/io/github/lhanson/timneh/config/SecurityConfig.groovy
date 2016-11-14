package io.github.lhanson.timneh.config

import io.github.lhanson.timneh.security.JWTAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired JWTAuthenticationFilter jwtAuthenticationFilter
	@Autowired AuthenticationEntryPoint authenticationEntryPoint

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
					.antMatchers(HttpMethod.POST, '/login').permitAll()
					.antMatchers(HttpMethod.GET, '/', '/index.html', '/home.html', '/error').permitAll()
					.anyRequest().authenticated()
					.and()
				 // JWT authentication
				.addFilterAfter(
					jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter)
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
