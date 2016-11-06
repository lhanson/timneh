package io.github.lhanson.timneh.controller

import io.github.lhanson.timneh.dao.DiscussionDao
import io.github.lhanson.timneh.domain.Discussion
import io.github.lhanson.timneh.domain.UserDetails
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
class DiscussionController extends AbstractController {
	Logger log = LoggerFactory.getLogger(this.class)
	@Autowired DiscussionDao discussionDao

	@GetMapping("/discussions")
	List<Discussion> discussions() {
		log.trace "Loading discussions"
		discussionDao.loadAll()
	}

	@GetMapping("/discussions/{id}")
	ResponseEntity<Discussion> discussion(@PathVariable('id') String id) {
		log.trace "Loading discussion $id"
		returnIfFound(discussionDao.loadById(Integer.valueOf(id)))
	}

	@PostMapping("/discussions")
	ResponseEntity<Void> createDiscussion(
			@RequestBody String title, Authentication authentication, UriComponentsBuilder builder) {
		def userDetails = (UserDetails) authentication.principal
		log.trace "Creating discussion titled \"$title\" for {}", userDetails
		def result = discussionDao.create(userDetails.id, title)

		// Set Location header to newly-created resource URI
		HttpHeaders headers = new HttpHeaders()
		headers.setLocation(builder.path("/discussions/{id}").buildAndExpand(result).toUri())

		new ResponseEntity<Void>(headers, HttpStatus.CREATED)
	}

}
