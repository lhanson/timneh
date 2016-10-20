package io.github.lhanson.timneh.dao

import io.github.lhanson.timneh.domain.UserDetails
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Repository

import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp

@Repository
class UserDao {
	Logger log = LoggerFactory.getLogger(this.class)
	@Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate
	@Autowired Map databaseQueries

	UserDetails loadUserById(int id) {
		log.trace "Loading user data for user '$id'"
		namedParameterJdbcTemplate.queryForObject(
				databaseQueries['loadUserById'], [id: id], new UserDetailsRowMapper())
	}

	UserDetails loadUserByUsername(String username) {
		log.trace "Loading user data for user '$username'"
		namedParameterJdbcTemplate.queryForObject(
				databaseQueries['loadUserByUsername'], [username: username], new UserDetailsRowMapper())
	}

	class UserDetailsRowMapper implements RowMapper<UserDetails> {
		@Override
		UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
			new UserDetails(
					1,
					'user',
					'first last',
					'password',
					'user@domain.tld',
					new Timestamp(new Date().time),
					[new SimpleGrantedAuthority('USER')]
			)
		}
	}

	List<GrantedAuthority> tokenizeAuthorities(String authorities) {
		authorities
				.tokenize(',')
				.collect { new SimpleGrantedAuthority(it) }
	}

}
