package io.github.lhanson.timneh.dao

import io.github.lhanson.timneh.domain.Discussion
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
			def result = [asMap(new Discussion()), asMap(new Discussion())]
			1 * jdbcTemplate.queryForList(_) >> result
		when:
			List<Discussion> discussions = discussionDao.loadAll()

		then:
			discussions
	}

	def "loadById"() {
		given:
			def result = asMap(new Discussion())
			1 * namedParameterJdbcTemplate.queryForMap(_, _) >> result
		when:
			Discussion discussion = discussionDao.loadById(1)

		then:
			discussion
	}

	def "create"() {
		given:
			discussionDao.insertDiscussion = Mock(SimpleJdbcInsert)
			1 * discussionDao.insertDiscussion.execute(_) >> 1
		when:
			def rowsAffected = discussionDao.create(1, 'Title')

		then:
			rowsAffected == 1
	}

	// For a given Object, return its properties as a map
	def asMap(Object o) {
		o.class.declaredFields.findAll { !it.synthetic }.collectEntries {
			[(it.name): o."$it.name"]
		}
	}

}
