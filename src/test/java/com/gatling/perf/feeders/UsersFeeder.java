package com.gatling.perf.feeders;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.CoreDsl;

public class UsersFeeder {
	public static final FeederBuilder<String> validUsers = CoreDsl.csv("data/users.csv").circular();
}
