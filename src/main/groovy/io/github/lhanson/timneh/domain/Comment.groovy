package io.github.lhanson.timneh.domain

import java.sql.Timestamp

/**
 * A Comment is a User's contribution to a Discussion
 */
class Comment {
	int id
	int author_id
	String text
	Timestamp created
	Timestamp modified
}
