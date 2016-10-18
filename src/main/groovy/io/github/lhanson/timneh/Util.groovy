package io.github.lhanson.timneh

/** Utility class containing general-purpose methods */
final class Util {
	private Util() {}

	/**
	 * Returns a map with the same values as the provided one, but with
	 * lowercased keys.
	 * @param m input map
	 * @return copy of the input map with all keys lowercased
	 */
	static Map lowercaseKeys(Map m) {
		Map result = [:]
		m.keySet().each { key ->
			result[key.toLowerCase()] = m[key]
		}
		result
	}

}
