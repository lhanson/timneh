package io.github.lhanson.timneh

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class DatabaseConfig {
	Logger log = LoggerFactory.getLogger(this.class)

	@Bean
	Properties databaseQueries(@Autowired Environment env) {
		Properties queries = new Properties()
		if (!env.activeProfiles) {
			log.debug "No active profiles, loading HSQLDB queries"
			queries.with {
				put('loadUserById',
						"""
						select users.*, group_concat(authority) authorities
						from users users
						join authorities auth
						  on users.id = auth.id
						where users.id= :id
						group by users.id
						""")
				put('loadUserByUsername',
						"""
						select users.*, group_concat(authority) authorities
						from users users
						join authorities auth
						  on users.id = auth.id
						where users.username = :username
						group by users.id
						""")
			}
		} else {
			log.debug "Loading PostgreSQL queries"
			queries.with {
				put('loadUserById',
						"""
						select users.*, string_agg(authority, ',') authorities
						from users users
						join authorities auth
						  on users.id = auth.id
						where users.id = :id
						group by users.id
						""")
				put('loadUserByUsername',
						"""
						select users.*, string_agg(authority, ',') authorities
						from users users
						join authorities auth
						  on users.id = auth.id
						where users.username = :username
						group by users.id
						""")
			}
		}
		queries
	}
}
