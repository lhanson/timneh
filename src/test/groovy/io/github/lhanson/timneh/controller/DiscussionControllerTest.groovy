package io.github.lhanson.timneh.controller

import io.github.lhanson.timneh.dao.DiscussionDao
import io.github.lhanson.timneh.domain.Discussion
import spock.lang.Specification

class DiscussionControllerTest extends Specification {
	DiscussionDao discussionDao
	DiscussionController discussionController

	def setup() {
		discussionDao = Mock()
		discussionController = new DiscussionController(discussionDao: discussionDao)
	}

	def "discussions() invokes DiscussionDao"() {
		given:
			def result = new Discussion()
			1 * discussionDao.loadAll() >> [result]

		when:
			def discussions = discussionController.discussions()

		then:
			discussions.contains result
	}

	def "load discussion by ID"() {
		given:
			def result = new Discussion(id: 1)
			1 * discussionDao.loadById(1) >> result

		when:
			def discussion = discussionController.discussion('1')

		then:
			discussion == result
	}

	def "load discussion by ID fails for invalid ID"() {
		given:
			def result = new Discussion(id: 1)
			0 * discussionDao.loadById(1) >> result

		when:
			def discussion = discussionController.discussion('2')

		then:
			discussion == null
	}

}
