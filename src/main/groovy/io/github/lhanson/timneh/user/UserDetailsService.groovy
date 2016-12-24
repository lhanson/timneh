package io.github.lhanson.timneh.user

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
