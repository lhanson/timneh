package io.github.lhanson.timneh.config

import io.github.lhanson.timneh.security.UserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

import javax.sql.DataSource

@Configuration
@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.httpBasic()
					.and()
				.authorizeRequests()
					.antMatchers('/', '/index.html', '/home.html', '/login.html').permitAll()
//					.anyRequest().authenticated()
					.and()
				.csrf()
					.disable()
	}

	@Bean
	@Profile(['default', 'local', 'travis-ci'])
	PasswordEncoder noopPasswordEncoder() {
		new NoOpPasswordEncoder()
	}

	@Bean
	@Profile('dev')
	PasswordEncoder bcryptPasswordEncoder() {
		new BCryptPasswordEncoder();
	}

	@Autowired
	void configureGlobal(AuthenticationManagerBuilder auth,
	                     Environment environment,
	                     DataSource dataSource,
	                     PasswordEncoder passwordEncoder,
	                     UserDetailsService userDetailsService) throws Exception {
		auth
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder)
		if (environment.activeProfiles.size() == 0) {
			auth
					.inMemoryAuthentication()
					.withUser('user')
					.password('password')
					.authorities('USER')
		} else {
			auth
					.jdbcAuthentication()
					.dataSource(dataSource)
		}
	}

}
