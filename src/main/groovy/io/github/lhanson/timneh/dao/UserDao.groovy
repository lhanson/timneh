package io.github.lhanson.timneh.dao

import io.github.lhanson.timneh.domain.UserDetails
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class UserDao {
	Logger log = LoggerFactory.getLogger(this.class)
	@Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate
	@Autowired Map databaseQueries
	UserDetailsRowMapper rowMapper = new UserDetailsRowMapper()

	UserDetails loadUserById(int id) {
		log.trace "Loading user data for user '$id'"
		UserDetails userDetails
		try {
			userDetails = namedParameterJdbcTemplate.queryForObject(
					databaseQueries['loadUserById'], [id: id], rowMapper)
		} catch (IncorrectResultSizeDataAccessException e) {
			handleIncorrectResultSize(e)
		}
		userDetails
	}

	UserDetails loadUserByUsername(String username) {
		log.trace "Loading user data for user '$username'"
		UserDetails userDetails
		try {
			userDetails = namedParameterJdbcTemplate.queryForObject(
				databaseQueries['loadUserByUsername'], [username: username], rowMapper)
		} catch (IncorrectResultSizeDataAccessException e) {
			handleIncorrectResultSize(e)
		}
		userDetails
	}

	private def handleIncorrectResultSize(IncorrectResultSizeDataAccessException e) {
		if (e.actualSize != 0) {
			// No results is expected for a bad username,
			// but we should never get more than one result
			throw e
		}
	}

}
