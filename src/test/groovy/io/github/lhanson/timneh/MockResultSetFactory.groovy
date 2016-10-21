package io.github.lhanson.timneh

import spock.lang.Specification

import java.sql.ResultSet

/**
 * Factory for a mock <code>ResultSet</code> used in a Spock test.
 */
class MockResultSetFactory extends Specification {
	/**
	 * @param columnNames columns names that tests will access in methods like resultSet.getString('column_name')
	 * @param rows data rows containing the data elements in the same order as columnNames
	 * @return the mock ResultSet usable in a Spock test
	 */
	static ResultSet makeResultSet(List<String> columnNames, List... rows) {
		new MockResultSetFactory().makeResultSetInternal(columnNames, rows)
	}

	private def ResultSet makeResultSetInternal(List<String> columnNames, List... rows) {
		ResultSet result = Mock()
		int currentIndex = -1

		result.next() >> { ++currentIndex < rows.length }
		// see https://code.google.com/archive/p/spock/wikis/Interactions.wiki
		// Method constraints section for a description of the following structure
		result./get(String|Short|Date|Int|Timestamp|BigDecimal)/(_) >> { String argument ->
			if (!(argument in columnNames)){
				return null
			}
			rows[currentIndex][columnNames.indexOf(argument)]
		}

		return result
	}
}
