package io.github.lhanson.timneh.user

import io.github.lhanson.timneh.AbstractDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.stereotype.Repository

@Repository
class UserDao extends AbstractDao {
	@Autowired Map databaseQueries
	UserDetailsRowMapper rowMapper = new UserDetailsRowMapper()

	UserDetails loadUserById(int id) {
		log.trace "Loading user data by ID for '$id'"
		UserDetails userDetails
		try {
			userDetails = namedParameterJdbcTemplate.queryForObject(
					databaseQueries['loadUserById'], [id: id], rowMapper)
		} catch (IncorrectResultSizeDataAccessException e) {
			handleExpectedZeroResult(e)
		}
		userDetails
	}

	UserDetails loadUserByUsername(String username) {
		log.trace "Loading user data by username for '$username'"
		UserDetails userDetails
		try {
			userDetails = namedParameterJdbcTemplate.queryForObject(
				databaseQueries['loadUserByUsername'], [username: username], rowMapper)
		} catch (IncorrectResultSizeDataAccessException e) {
			handleExpectedZeroResult(e)
		}
		userDetails
	}

}
