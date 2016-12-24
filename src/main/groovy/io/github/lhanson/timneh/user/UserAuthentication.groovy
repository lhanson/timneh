package io.github.lhanson.timneh.user

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class UserAuthentication implements Authentication {
	UserDetails user
	boolean authenticated = true

	@Override
	Collection<? extends GrantedAuthority> getAuthorities() {
		user.authorities
	}

	@Override
	Object getCredentials() {
		user.password
	}

	@Override
	Object getDetails() {
		user
	}

	@Override
	Object getPrincipal() {
		user
	}

	@Override
	String getName() {
		user.username
	}
}
