package io.github.lhanson.timneh.domain

import java.sql.Timestamp

/**
 * A timneh user
 */
class User {
	int id
	String username
	String fullName
	String emailAddress
	Timestamp created

	User(UserDetails userDetails) {
		this.id = userDetails.id
		this.username = userDetails.username
		this.fullName = userDetails.fullName
		this.emailAddress = userDetails.emailAddress
		this.created = userDetails.created
	}
}
