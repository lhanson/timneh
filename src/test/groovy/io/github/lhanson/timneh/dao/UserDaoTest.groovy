package io.github.lhanson.timneh.dao

import io.github.lhanson.timneh.domain.UserDetails
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.core.authority.SimpleGrantedAuthority
import spock.lang.Specification

import java.sql.Timestamp

class UserDaoTest extends Specification {
	UserDao userDao
	JdbcTemplate jdbcTemplate
	def jdbcResult

	def setup() {
		jdbcTemplate = Mock()
		userDao = new UserDao(jdbcTemplate: jdbcTemplate)
		jdbcResult = [
				id: 1,
				username: 'username',
				password: '12345',
				full_name: 'First Last',
				email: 'user@domain.tld',
				enabled: true,
				created: new Timestamp(new Date().time),
				authorities: 'TEST_USER']
	}

	def "loadUserById returns the correct result"() {
		given:
			1 * jdbcTemplate.queryForMap(_, _) >> jdbcResult

		when:
			UserDetails user = userDao.loadUserById(jdbcResult.id)

		then:
			user.id           == jdbcResult.id
			user.username     == jdbcResult.username
			user.password     == jdbcResult.password
			user.fullName     == jdbcResult.fullName
			user.emailAddress == jdbcResult.emailAddress
			user.created      == jdbcResult.created
			// The JDBC result set is a delimited string, so verify that
			// it gets mapped into the proper collection
			user.authorities.toList() == userDao.tokenizeAuthorities(jdbcResult.authorities)
	}

	def "loadUserById with multiple authorities"() {
		given:
			jdbcResult.authorities = 'TEST_USER,TEST_ADMIN'
			1 * jdbcTemplate.queryForMap(_, _) >> jdbcResult

		when:
			UserDetails user = userDao.loadUserById(jdbcResult.id)

		then:
			user.authorities.contains(new SimpleGrantedAuthority('TEST_USER'))
			user.authorities.contains(new SimpleGrantedAuthority('TEST_ADMIN'))
	}

	def "loadUserByUsername returns the correct result"() {
		given:
			1 * jdbcTemplate.queryForMap(_, _) >> jdbcResult

		when:
			UserDetails user = userDao.loadUserByUsername(jdbcResult.username)

		then:
			user.id           == jdbcResult.id
			user.username     == jdbcResult.username
			user.password     == jdbcResult.password
			user.fullName     == jdbcResult.fullName
			user.emailAddress == jdbcResult.emailAddress
			user.created      == jdbcResult.created
			// The JDBC result set is a delimited string, so verify that
			// it gets mapped into the proper collection
			user.authorities.toList() == userDao.tokenizeAuthorities(jdbcResult.authorities)
	}

}
