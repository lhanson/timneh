package io.github.lhanson.timneh.dao

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import spock.lang.Specification

class CommentDaoTest extends Specification {
	CommentDao commentDao
	JdbcTemplate jdbcTemplate
	NamedParameterJdbcTemplate namedParameterJdbcTemplate

	def setup() {
		jdbcTemplate = Mock()
		namedParameterJdbcTemplate = Mock()
		commentDao = new CommentDao(
				jdbcTemplate: jdbcTemplate,
				namedParameterJdbcTemplate: namedParameterJdbcTemplate)
	}

	def "create"() {
		given:
			commentDao.insertDiscussion = Mock(SimpleJdbcInsert)
			1 * commentDao.insertDiscussion.executeAndReturnKey(_) >> 432
		when:
			def newCommentId = commentDao.create(1, 1, 'test comment')

		then:
			newCommentId == 432
	}

}
