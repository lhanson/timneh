package io.github.lhanson.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import java.time.LocalDateTime

@RestController
class Index {

	@RequestMapping("/")
	public String index() {
		"Greetings from Spring Boot!"
	}

	@RequestMapping("/now")
	public Map<String, Object> localDateTime() {
		['id': 1234, 'content': LocalDateTime.now().toString()]
	}

}
