package io.github.lhanson.timneh.dao

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

import javax.annotation.PostConstruct

@Repository
class CommentDao {
	Logger log = LoggerFactory.getLogger(this.class)
	@Autowired JdbcTemplate jdbcTemplate
	@Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate
	SimpleJdbcInsert insertDiscussion

	@PostConstruct
	void init() {
		insertDiscussion = new SimpleJdbcInsert(jdbcTemplate.dataSource)
				.withTableName('comments')
				.usingGeneratedKeyColumns('id')
	}

	int create(int authorId, int discussionId, String comment) {
		log.trace "Creating comment in discussion $discussionId for user $authorId"
		insertDiscussion.executeAndReturnKey([author_id: authorId, discussion_id: discussionId, text: comment])
	}

}
