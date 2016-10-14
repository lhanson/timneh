package io.github.lhanson.config

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
					.anyRequest().authenticated()
					.and()
				.csrf()
					.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}

	@Bean
	@Profile(['default', 'local'])
	public PasswordEncoder noopPasswordEncoder() {
		new NoOpPasswordEncoder()
	}

	@Bean
	@Profile('dev')
	public PasswordEncoder bcryptPasswordEncoder() {
		new BCryptPasswordEncoder();
	}

	@Autowired
	void configureGlobal(AuthenticationManagerBuilder auth,
	                     Environment environment,
	                     DataSource dataSource,
	                     PasswordEncoder passwordEncoder) throws Exception {
		if (environment.activeProfiles.size() == 0) {
			auth
					.inMemoryAuthentication()
					.withUser('user')
					.password('password')
					.authorities('USER')
		} else {
			auth
					.jdbcAuthentication()
					.passwordEncoder(passwordEncoder)
					.dataSource(dataSource)
		}
	}

}
