package io.github.lhanson.timneh.security

import io.github.lhanson.timneh.dao.UserDao
import io.github.lhanson.timneh.domain.UserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
	@Autowired UserDao userDao

	@Override
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		userDao.loadUserByUsername(username)
	}
}
