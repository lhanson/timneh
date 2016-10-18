package io.github.lhanson.timneh.controller

import io.github.lhanson.timneh.dao.DiscussionDao
import io.github.lhanson.timneh.domain.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import java.time.LocalDateTime

@RestController
class UserController {
	Logger log = LoggerFactory.getLogger(this.class)
	@Autowired DiscussionDao discussionDao

	@RequestMapping("/now")
	Map<String, Object> localDateTime() {
		['id': 1234, 'content': LocalDateTime.now().toString()]
	}

	@RequestMapping("/user")
	User user(Authentication authentication) {
		log.trace("Loaded authentication {}", authentication)
		new User(authentication.principal)
	}

}
