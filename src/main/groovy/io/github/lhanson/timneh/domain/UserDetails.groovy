package io.github.lhanson.timneh.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

/**
 * Extension of Spring Security's {@link User} to include domain-specific fields
 */
class UserDetails extends User {
	String fullName
	String emailAddress

	UserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities)
	}

	UserDetails(String username, String fullName, String password, String emailAddress, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities)
		this.fullName = fullName
		this.emailAddress = emailAddress
	}
}
