package io.github.lhanson.timneh.controller

import io.github.lhanson.timneh.dao.DiscussionDao
import io.github.lhanson.timneh.domain.Discussion
import io.github.lhanson.timneh.domain.UserDetails
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.web.util.UriComponentsBuilder
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

	def "load discussion by ID returns 404 for invalid ID"() {
		given:
			def result = new Discussion(id: 1)
			0 * discussionDao.loadById(1) >> result

		when:
			ResponseEntity response = discussionController.discussion('2')

		then:
			response
			response.statusCode == HttpStatus.NOT_FOUND
	}

	def "create discussion returns the ID of the newly created discussion"() {
		given:
			def userId = 12
			def newDiscussionId = 432
			UserDetails testUser = new UserDetails('username', 'password', [])
			testUser.id = userId
			1 * discussionDao.create(userId, 'New Discussion') >> newDiscussionId

		when:
			ResponseEntity result = discussionController.createDiscussion(
					'New Discussion',
					new TestingAuthenticationToken(testUser, null),
					new UriComponentsBuilder())

		then:
			result.statusCode == HttpStatus.CREATED
			result.headers['Location'] == ["/discussions/$newDiscussionId"]
	}

}
