package io.github.lhanson.timneh

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert

/**
 * Superclass for DAOs having common functionality
 */
class AbstractDao {
	Logger log = LoggerFactory.getLogger(this.class)
	@Autowired JdbcTemplate jdbcTemplate
	@Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate
	SimpleJdbcInsert insertDiscussion

	def handleExpectedZeroResult(IncorrectResultSizeDataAccessException e) {
		if (e.actualSize != 0) {
			// No results are expected for nonexistent entity,
			// but we should never get more than one result
			throw e
		}
	}
}
