package io.github.lhanson.timneh.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

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
