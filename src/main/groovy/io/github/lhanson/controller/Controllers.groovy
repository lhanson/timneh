package io.github.lhanson.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import java.security.Principal
import java.time.LocalDateTime

@RestController
class Controllers {

	@RequestMapping("/now")
	Map<String, Object> localDateTime() {
		['id': 1234, 'content': LocalDateTime.now().toString()]
	}

	@RequestMapping("/user")
	Principal user(Principal user) {
		user
	}
}
