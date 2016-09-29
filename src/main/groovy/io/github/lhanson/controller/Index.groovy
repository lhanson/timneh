package io.github.lhanson.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Index {

	@RequestMapping("/")
	public String index() {
		"Greetings from Spring Boot!"
	}

}
