package io.github.lhanson.timneh.controller

import io.github.lhanson.timneh.dao.CommentDao
import io.github.lhanson.timneh.domain.Comment
import io.github.lhanson.timneh.domain.UserDetails
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Specification

class CommentControllerTest extends Specification {
	CommentController commentController
	CommentDao commentDao

	def setup() {
		commentDao = Mock()
		commentController = new CommentController(commentDao: commentDao)
	}

	def "add comment returns the ID of the newly created comment"() {
		given:
			def newCommentId = 42
			def discussionId = 84
			def commentText = 'First post, brah!'
			UserDetails testUser = new UserDetails('username', 'password', [])
			testUser.id = 21
			1 * commentDao.create(testUser.id, discussionId, commentText) >> newCommentId

		when:
			// post discussion ID and text, everything else is known by the server
			ResponseEntity response = commentController.createComment(
					discussionId,
					commentText,
					new TestingAuthenticationToken(testUser, null),
					new UriComponentsBuilder())

		then:
			response.statusCode == HttpStatus.CREATED
			response.headers['Location'] == ["/comments/$discussionId/$newCommentId"]
	}

	def "load comments by discussion id"() {
		given:
			def comments = [new Comment(id: 1)]
			1 * commentDao.loadByDiscussionId(1) >> comments

		when:
			ResponseEntity response = commentController.comments(1)

		then:
			response.statusCode == HttpStatus.OK
			response.body == comments
	}

	def "load comments by invalid discussion id returns 404"() {
		given:
			1 * commentDao.loadByDiscussionId(2) >> null

		when:
			ResponseEntity response = commentController.comments(2)

		then:
			response.statusCode == HttpStatus.NOT_FOUND
	}

}
