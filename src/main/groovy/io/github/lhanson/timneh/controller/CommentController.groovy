package io.github.lhanson.timneh.controller

import io.github.lhanson.timneh.dao.CommentDao
import io.github.lhanson.timneh.domain.UserDetails
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
class CommentController {
	Logger log = LoggerFactory.getLogger(this.class)
	@Autowired CommentDao commentDao

	@PostMapping("/comments/{discussionId}")
	ResponseEntity<Void> createComment(
			@PathVariable('discussionId') Integer discussionId, @RequestBody String comment,
			Authentication authentication, UriComponentsBuilder builder) {
		def userDetails = (UserDetails) authentication.principal
		log.trace "Posting comment to discussion $discussionId for {}", userDetails
		def result = commentDao.create(userDetails.id, discussionId, comment)

		// Set Location header to newly-created resource URI
		HttpHeaders headers = new HttpHeaders()
		headers.setLocation(builder.path("/comments/$discussionId/{id}").buildAndExpand(result).toUri())

		new ResponseEntity<Void>(headers, HttpStatus.CREATED)
	}

}
