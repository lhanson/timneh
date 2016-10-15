package io.github.lhanson.timneh.domain

/**
 * A timneh user
 */
class User {
	String username
	String fullName
	String emailAddress

	User(UserDetails userDetails) {
		this.username = userDetails.username
		this.fullName = userDetails.fullName
		this.emailAddress = userDetails.emailAddress
	}
}
