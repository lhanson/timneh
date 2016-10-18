package io.github.lhanson.timneh

final class TestUtil {
	private TestUtil() { }

	// For a given Object, return its properties as a map
	static Map asMap(Object o) {
		o.class.declaredFields.findAll { !it.synthetic }.collectEntries {
			[(it.name): o."$it.name"]
		}
	}

}
