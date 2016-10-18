package io.github.lhanson.timneh.domain

import java.sql.Timestamp

/**
 * A discussion is a topic of conversation to which Comments are associated
 */
class Discussion {
	int id
	int author_id
	String title
	Timestamp created
	Timestamp modified
}
