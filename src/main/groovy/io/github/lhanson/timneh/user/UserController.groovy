package io.github.lhanson.timneh.user

import io.github.lhanson.timneh.discussion.DiscussionDao
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {
	Logger log = LoggerFactory.getLogger(this.class)
	@Autowired DiscussionDao discussionDao

	@GetMapping("/user")
	User user(Authentication authentication) {
		log.trace("Loaded authentication {}", authentication)
		new User(authentication.principal)
	}
}
