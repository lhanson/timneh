package io.github.lhanson.timneh

import org.springframework.http.ResponseEntity

import static org.springframework.http.HttpStatus.*

/**
 * Superclass for controllers having common functionality
 */
class AbstractController {
	ResponseEntity returnIfFound(Object payload) {
		if (payload) {
			new ResponseEntity<>(payload, OK)
		}
		else {
			new ResponseEntity<>(NOT_FOUND)
		}
	}
}
