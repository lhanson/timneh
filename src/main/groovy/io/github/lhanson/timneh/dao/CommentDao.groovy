package io.github.lhanson.timneh.dao

import io.github.lhanson.timneh.domain.Comment
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
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
	RowMapper<Comment> rowMapper = BeanPropertyRowMapper.newInstance(Comment)

	@PostConstruct
	void init() {
		insertDiscussion = new SimpleJdbcInsert(jdbcTemplate.dataSource)
				.withTableName('comments')
				.usingColumns('author_id', 'discussion_id', 'text')
				.usingGeneratedKeyColumns('id')
	}

	List<Comment> loadByDiscussionId(int discussionId) {
		log.trace "Loading comments for discussion $discussionId"
		namedParameterJdbcTemplate.query(
				'select * from comments where discussion_id=:id', [id: discussionId], rowMapper)
	}

	int create(int authorId, int discussionId, String comment) {
		log.trace "Creating comment in discussion $discussionId for user $authorId"
		insertDiscussion.executeAndReturnKey([author_id: authorId, discussion_id: discussionId, text: comment])
	}

}
