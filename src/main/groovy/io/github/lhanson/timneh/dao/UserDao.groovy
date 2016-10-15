package io.github.lhanson.timneh.dao

import io.github.lhanson.timneh.domain.UserDetails
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

import java.sql.Timestamp

@Repository
class UserDao {
	Logger log = LoggerFactory.getLogger(this.class)
	@Autowired JdbcTemplate jdbcTemplate

	UserDetails loadUserById(String username) {
		log.trace "Loading user data for user '$username'"
		new UserDetails(1, 'test_username', 'Firstname Lastname', 'test_password', 'email@foo.com', new Timestamp(new Date().time), [])
	}

	UserDetails loadUserByUsername(String username) {
		log.trace "Loading user data for user '$username'"
		new UserDetails(1, 'test_username', 'Firstname Lastname', 'test_password', 'email@foo.com', new Timestamp(new Date().time), [])
	}

}
