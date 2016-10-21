package io.github.lhanson.timneh.dao

import io.github.lhanson.timneh.domain.UserDetails
import org.springframework.security.core.authority.SimpleGrantedAuthority
import spock.lang.Specification

import java.sql.Timestamp

import static io.github.lhanson.timneh.MockResultSetFactory.*

class UserDetailsRowMapperTest extends Specification {

	def "UserDetails rows are mapped correctly"() {
		given:
			UserDetails sampleUserDetails = new UserDetails(
					1,
					'test_username',
					'Firstname Lastname',
					'test_password',
					'test_email@domain.tld',
					new Timestamp(new Date().time),
					[new SimpleGrantedAuthority('USER'), new SimpleGrantedAuthority('ADMIN')])
			UserDetailsRowMapper rowMapper = new UserDetailsRowMapper()

			def resultSet
			sampleUserDetails.with {
				resultSet = makeResultSet(
						['id', 'username', 'full_name', 'email', 'password', 'created', 'authorities'],
						[id, username, fullName, emailAddress, password, created, 'USER,ADMIN'])
			}

		when:
			UserDetails result = rowMapper.mapRow(resultSet, 1)

		then:
			result.id           == sampleUserDetails.id
			result.username     == sampleUserDetails.username
			result.fullName     == sampleUserDetails.fullName
			result.password     == sampleUserDetails.password
			result.emailAddress == sampleUserDetails.emailAddress
			result.authorities  == sampleUserDetails.authorities
	}

}
