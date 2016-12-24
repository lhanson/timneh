package io.github.lhanson.timneh.user

import org.springframework.jdbc.core.RowMapper
import org.springframework.security.core.authority.SimpleGrantedAuthority

import java.sql.ResultSet
import java.sql.SQLException

class UserDetailsRowMapper implements RowMapper<UserDetails> {
	@Override
	UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		new UserDetails(
				rs.getInt('id'),
				rs.getString('username'),
				rs.getString('full_name'),
				rs.getString('password'),
				rs.getString('email'),
				rs.getTimestamp('created'),
				rs.getString('authorities')
						.tokenize(',')
						.collect { new SimpleGrantedAuthority(it) }
		)
	}
}
