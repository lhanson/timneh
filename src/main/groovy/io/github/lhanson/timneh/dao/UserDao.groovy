package io.github.lhanson.timneh.dao

import io.github.lhanson.timneh.domain.UserDetails
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Repository

@Repository
class UserDao {
	Logger log = LoggerFactory.getLogger(this.class)
	@Autowired JdbcTemplate jdbcTemplate

	UserDetails loadUserById(int id) {
		log.trace "Loading user data for user '$id'"
		Map result = jdbcTemplate.queryForMap("""
				select users.*, string_agg(authority, ',') authorities
				from users users
				join authorities auth
				  on users.id = auth.id
				where users.id= :id
				group by users.id""", [id: id])
		mapUserDetails(result)
	}

	UserDetails loadUserByUsername(String username) {
		log.trace "Loading user data for user '$username'"
		Map result = jdbcTemplate.queryForMap("""
				select users.*, string_agg(authority, ',') authorities
				from users users
				join authorities auth
				  on users.id = auth.id
				where users.username = :username
				group by users.id""", [username: username]);
		mapUserDetails(result)
	}

	UserDetails mapUserDetails(Map m) {
		new UserDetails(
				m.id,
				m.username,
				m.fullName,
				m.password,
				m.email_address,
				m.created,
				tokenizeAuthorities(m.authorities)
		)
	}

	List<GrantedAuthority> tokenizeAuthorities(String authorities) {
		authorities
				.tokenize(',')
				.collect { new SimpleGrantedAuthority(it) }
	}

}
