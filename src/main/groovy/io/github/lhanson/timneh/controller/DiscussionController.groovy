package io.github.lhanson.timneh.controller

import io.github.lhanson.timneh.dao.DiscussionDao
import io.github.lhanson.timneh.domain.Discussion
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class DiscussionController {
	Logger log = LoggerFactory.getLogger(this.class)
	@Autowired DiscussionDao discussionDao

	@GetMapping("/discussions")
	List<Discussion> discussions() {
		log.trace "Loading discussions"
		discussionDao.loadAll()
	}

	@GetMapping("/discussions/{id}")
	Discussion discussion(@PathVariable('id') String id) {
		log.trace "Loading discussion $id"
		discussionDao.loadById(Integer.valueOf(id))
	}

	@PostMapping("/discussions")
	def createDiscussion(@RequestBody String title) {
		log.trace "Creating discussion titled $title"
	}

}
