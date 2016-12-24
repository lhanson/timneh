package io.github.lhanson.timneh.discussion

import io.github.lhanson.timneh.AbstractDao
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

import javax.annotation.PostConstruct

@Repository
class DiscussionDao extends AbstractDao {
	RowMapper<Discussion> rowMapper = BeanPropertyRowMapper.newInstance(Discussion)

	@PostConstruct
	void init() {
		insertDiscussion = new SimpleJdbcInsert(jdbcTemplate.dataSource)
				.withTableName('discussions')
				.usingColumns('author_id', 'title')
				.usingGeneratedKeyColumns('id')
	}

	List<Discussion> loadAll() {
		log.trace "Loading all discussions"
		jdbcTemplate.query('select * from discussions', rowMapper)
	}

	Discussion loadById(int id) {
		log.trace "Loading discussion $id"
		Discussion discussion
		try {
			discussion = namedParameterJdbcTemplate.queryForObject(
				'select * from discussions where id=:id', [id: id], rowMapper)
		} catch (IncorrectResultSizeDataAccessException e) {
			handleExpectedZeroResult(e)
		}
		discussion
	}

	int create(int authorId, String title) {
		log.trace "Creating discussion named $title for $authorId"
		insertDiscussion.executeAndReturnKey([author_id: authorId, title: title])
	}

}
