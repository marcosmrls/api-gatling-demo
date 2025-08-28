package com.gatling.perf.feeders;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DynamicUsersFeeder {

	// Devuelve un Supplier que, cada vez que Gatling lo llame,
	// crea un iterador NUEVO e infinito de usuarios únicos.
	public static Supplier<Iterator<Map<String, Object>>> uniqueUsers() {
		return () -> Stream.generate(() -> {
			var tag = UUID.randomUUID().toString().substring(0, 8);
			return Map.<String, Object>of(
					"name", "User-" + tag,
					"email", "perf-" + tag + "@mrcs-demo.com",
					"role",	"user");
		}).iterator();
	}
}
