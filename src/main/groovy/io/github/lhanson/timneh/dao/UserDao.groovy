package io.github.lhanson.timneh.dao

import io.github.lhanson.timneh.domain.UserDetails
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
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
		namedParameterJdbcTemplate.queryForObject(
				databaseQueries['loadUserById'], [id: id], rowMapper)
	}

	UserDetails loadUserByUsername(String username) {
		log.trace "Loading user data for user '$username'"
		namedParameterJdbcTemplate.queryForObject(
				databaseQueries['loadUserByUsername'], [username: username], rowMapper)
	}

}
