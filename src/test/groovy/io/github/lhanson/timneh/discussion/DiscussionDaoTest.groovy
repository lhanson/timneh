package io.github.lhanson.timneh.discussion

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import spock.lang.Specification

class DiscussionDaoTest extends Specification {
	DiscussionDao discussionDao
	JdbcTemplate jdbcTemplate
	NamedParameterJdbcTemplate namedParameterJdbcTemplate

	def setup() {
		jdbcTemplate = Mock()
		namedParameterJdbcTemplate = Mock()
		discussionDao = new DiscussionDao(
				jdbcTemplate: jdbcTemplate,
				namedParameterJdbcTemplate: namedParameterJdbcTemplate)
	}

	def "loadAll"() {
		given:
			def result = [new Discussion(), new Discussion()]
			1 * jdbcTemplate.query(_, _) >> result
		when:
			List<Discussion> discussions = discussionDao.loadAll()

		then:
			discussions
	}

	def "loadAll with no results returns null"() {
		given:
			1 * jdbcTemplate.query(_, _) >> null
		when:
			def result = discussionDao.loadAll()

		then:
			result == null
	}

	def "loadById"() {
		given:
			def result = new Discussion()
			1 * namedParameterJdbcTemplate.queryForObject(_, _, _) >> result
		when:
			Discussion discussion = discussionDao.loadById(1)

		then:
			discussion
	}

	def "loadById with no results returns null"() {
		given:
			1 * namedParameterJdbcTemplate.queryForObject(_, _, _) >> null
		when:
			def result = discussionDao.loadById(1)

		then:
			result == null
	}

	def "create"() {
		given:
			discussionDao.insertDiscussion = Mock(SimpleJdbcInsert)
			1 * discussionDao.insertDiscussion.executeAndReturnKey(_) >> 432
		when:
			def newDiscussionId = discussionDao.create(1, 'Title')

		then:
			newDiscussionId == 432
	}

}
