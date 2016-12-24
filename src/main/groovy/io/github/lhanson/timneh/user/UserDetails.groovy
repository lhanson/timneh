package io.github.lhanson.timneh.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

import java.sql.Timestamp

/**
 * Extension of Spring Security's {@link User} to include domain-specific fields
 */
class UserDetails extends User {
	int id = -1
	String fullName
	String emailAddress
	Timestamp created

	UserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities)
	}

	UserDetails(int id, String username, String fullName, String password, String emailAddress, Timestamp created, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities)
		this.id = id
		this.fullName = fullName
		this.emailAddress = emailAddress
		this.created = created
	}
}
