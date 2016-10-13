package io.github.lhanson.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import java.security.Principal
import java.time.LocalDateTime

@RestController
class Controllers {
	Logger log = LoggerFactory.getLogger(this.class)

	@RequestMapping("/now")
	Map<String, Object> localDateTime() {
		['id': 1234, 'content': LocalDateTime.now().toString()]
	}

	@RequestMapping("/user")
	Principal user(Principal user) {
		log.debug("Returning user {}", user)
		user
	}
}
