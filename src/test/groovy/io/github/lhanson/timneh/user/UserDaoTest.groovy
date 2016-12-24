package io.github.lhanson.timneh.user

import io.github.lhanson.timneh.user.UserDao
import io.github.lhanson.timneh.user.UserDetails
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.security.core.authority.SimpleGrantedAuthority
import spock.lang.Specification

import java.sql.Timestamp

class UserDaoTest extends Specification {
	UserDao userDao
	NamedParameterJdbcTemplate namedParameterJdbcTemplate
	UserDetails jdbcResult

	def setup() {
		namedParameterJdbcTemplate = Mock()
		userDao = new UserDao(namedParameterJdbcTemplate: namedParameterJdbcTemplate, databaseQueries: [:])
		jdbcResult = new UserDetails(
				1, 'username', 'First Last', 'password', 'user@domain.tld', new Timestamp(new Date().time),
				[new SimpleGrantedAuthority('TEST_USER')])
	}

	def "loadUserById returns the correct result"() {
		given:
			1 * namedParameterJdbcTemplate.queryForObject(_, _, _) >> jdbcResult

		when:
			UserDetails user = userDao.loadUserById(jdbcResult.id)

		then:
			user.id           == jdbcResult.id
			user.username     == jdbcResult.username
			user.password     == jdbcResult.password
			user.fullName     == jdbcResult.fullName
			user.emailAddress == jdbcResult.emailAddress
			user.created      == jdbcResult.created
			user.authorities.toList() == jdbcResult.authorities.toList()
	}

	def "loadUserById with an invalid ID returns null"() {
		given:
			IncorrectResultSizeDataAccessException exception =
					new IncorrectResultSizeDataAccessException(1, 0)
			1 * namedParameterJdbcTemplate.queryForObject(_, _, _) >> { throw exception }

		when:
			UserDetails user = userDao.loadUserById(12345)

		then:
			user == null
	}

	def "loadUserById with multiple authorities"() {
		given:
			jdbcResult = new UserDetails(
					1, 'username', 'First Last', 'password', 'user@domain.tld', new Timestamp(new Date().time),
					[new SimpleGrantedAuthority('TEST_USER'), new SimpleGrantedAuthority('TEST_ADMIN')])
			1 * namedParameterJdbcTemplate.queryForObject(_, _, _) >> jdbcResult

		when:
			UserDetails user = userDao.loadUserById(jdbcResult.id)

		then:
			user.authorities.contains(new SimpleGrantedAuthority('TEST_USER'))
			user.authorities.contains(new SimpleGrantedAuthority('TEST_ADMIN'))
	}

	def "loadUserByUsername returns the correct result"() {
		given:
			1 * namedParameterJdbcTemplate.queryForObject(_, _, _) >> jdbcResult

		when:
			UserDetails user = userDao.loadUserByUsername(jdbcResult.username)

		then:
			user.id           == jdbcResult.id
			user.username     == jdbcResult.username
			user.password     == jdbcResult.password
			user.fullName     == jdbcResult.fullName
			user.emailAddress == jdbcResult.emailAddress
			user.created      == jdbcResult.created
			user.authorities.toList() == jdbcResult.authorities.toList()
	}

	def "loadUserByUsername with invalid user returns null"() {
		given:
			IncorrectResultSizeDataAccessException exception =
					new IncorrectResultSizeDataAccessException(1, 0)
			1 * namedParameterJdbcTemplate.queryForObject(_, _, _) >> { throw exception }

		when:
			UserDetails user = userDao.loadUserByUsername('invalid_username')

		then:
			user == null
	}

}
