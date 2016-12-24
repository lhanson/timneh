package io.github.lhanson.timneh.comment

import io.github.lhanson.timneh.AbstractController
import io.github.lhanson.timneh.user.UserAuthentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
class CommentController extends AbstractController {
	Logger log = LoggerFactory.getLogger(this.class)
	@Autowired CommentDao commentDao

	@GetMapping("/comments/{discussionId}")
	ResponseEntity<Comment> comments(@PathVariable('discussionId') Integer discussionId) {
		returnIfFound(commentDao.loadByDiscussionId(discussionId))
	}

	@PostMapping("/comments/{discussionId}")
	ResponseEntity<Void> createComment(
			@PathVariable('discussionId') Integer discussionId, @RequestBody String comment,
			UserAuthentication authentication, UriComponentsBuilder builder) {
		log.trace "Posting comment to discussion $discussionId for {}", authentication.user
		def result = commentDao.create(authentication.user.id, discussionId, comment)

		// Set Location header to newly-created resource URI
		HttpHeaders headers = new HttpHeaders()
		headers.setLocation(builder.path("/comments/$discussionId/{id}").buildAndExpand(result).toUri())

		new ResponseEntity<Void>(headers, HttpStatus.CREATED)
	}

}
