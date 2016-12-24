package io.github.lhanson.timneh.discussion

import java.sql.Timestamp

/**
 * A Discussion is a topic of conversation to which Comments are associated
 */
class Discussion {
	int id
	int author_id
	String title
	Timestamp created
	Timestamp modified
}
