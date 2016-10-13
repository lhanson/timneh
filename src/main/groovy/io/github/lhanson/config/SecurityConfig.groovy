package io.github.lhanson.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@Configuration
@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {
	Logger log = LoggerFactory.getLogger(this.class)

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.httpBasic()
					.and()
				.authorizeRequests()
					.antMatchers("/index.html", "/home.html", "/login.html", "/", "/user").permitAll()
					.anyRequest().authenticated()
					.and()
				.csrf()
					.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			@Override
			UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				log.trace "Loading user details for $username"
				new TimnehUserDetails(username: username)
			}
		}
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		new BCryptPasswordEncoder()
	}

	@Autowired
	void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.inMemoryAuthentication()
				.withUser("user").password("password").roles("USER")
	}

	class TimnehUserDetails implements UserDetails {
		String username
		String password

		@Override
		Collection<? extends GrantedAuthority> getAuthorities() {
			[]
		}

		@Override
		String getPassword() {
			password
		}

		@Override
		String getUsername() {
			username
		}

		@Override
		boolean isAccountNonExpired() {
			true
		}

		@Override
		boolean isAccountNonLocked() {
			true
		}

		@Override
		boolean isCredentialsNonExpired() {
			true
		}

		@Override
		boolean isEnabled() {
			true
		}
	}

}
