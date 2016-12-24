package io.github.lhanson.timneh.user

import spock.lang.Specification

class UserDetailsServiceTest extends Specification {
	UserDao userDao
	UserDetailsService userDetailsService
	UserDetails testUser = new UserDetails('username', 'password', [])

	def setup() {
		userDao = Mock()
		userDetailsService = new UserDetailsService(userDao: userDao)
	}

	def "loadByUsername loads the appropriate user"() {
		given:
			1 * userDao.loadUserByUsername(testUser.username) >> testUser

		when:
			UserDetails result = userDetailsService.loadUserByUsername(testUser.username)

		then:
			result.username == testUser.username
	}

	def "loadByUsername doesn't call DAO with incorrect username"() {
		given:
			0 * userDao.loadUserByUsername(testUser.username) >> testUser
			UserDetailsService userDetailsService = new UserDetailsService(userDao: userDao)

		when:
			UserDetails result = userDetailsService.loadUserByUsername('different username')

		then:
			result == null
	}

}
