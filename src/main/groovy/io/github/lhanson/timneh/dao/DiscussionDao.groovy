package io.github.lhanson.timneh.dao

import io.github.lhanson.timneh.domain.Discussion
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

import javax.annotation.PostConstruct

import static io.github.lhanson.timneh.Util.lowercaseKeys

@Repository
class DiscussionDao {
	Logger log = LoggerFactory.getLogger(this.class)
	@Autowired JdbcTemplate jdbcTemplate
	@Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate
	SimpleJdbcInsert insertDiscussion

	@PostConstruct
	void init() {
		insertDiscussion = new SimpleJdbcInsert(jdbcTemplate.dataSource).withTableName('discussions')
	}

	List<Discussion> loadAll() {
		log.trace "Loading all discussions"
		def results = []
		jdbcTemplate.queryForList('select * from discussions').collect { row ->
			results << new Discussion(lowercaseKeys(row))
		}
		results
	}

	Discussion loadById(int id) {
		log.trace "Loading discussion $id"
		Map result = namedParameterJdbcTemplate.queryForMap('select * from discussions where id=:id', [id: id])
		new Discussion(lowercaseKeys(result))
	}

	int create(int authorId, String title) {
		log.trace "Creating discussion named $title for $authorId"
		insertDiscussion.execute([author_id: authorId, title: title])
	}

}
