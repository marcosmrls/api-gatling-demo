package com.gatling.perf.scenarios;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class CommonChains {

	// Health
	public static final ChainBuilder health = exec(http("Health")
			.get("/health")
			.check(status().is(200)));

	// Auth: Login (admin)
	public static final ChainBuilder loginAdmin = feed(csv("data/admin.csv").circular())
			.exec(http("Auth - Login (admin)")
					.post("/auth/login")
					.body(StringBody("{\"email\":\"#{email}\",\"password\":\"#{password}\"}"))
					.check(status().is(200))
					.check(jmesPath("token").saveAs("adminToken")));

	// Users - Create
	public static final ChainBuilder createUser = exec(http("Users - Create #{email}")
			.post("/users")
			.header("Authorization", "Bearer #{adminToken}")
			.body(ElFileBody("bodies/user-create.json")).asJson()
			.check(status().is(201))
			.check(jmesPath("id").saveAs("createdUserId")));

	// Users - Get by ID
	public static final ChainBuilder getUserById = exec(http("Users - Get by ID - #{createdUserId}")
			.get("/users/#{createdUserId}")
			.header("Authorization", "Bearer #{adminToken}")
			.check(status().is(200))
			.check(jmesPath("id").isEL("#{createdUserId}")));

	// Security - Missing token -> 401
	public static final ChainBuilder missingToken = exec(http("Security - Missing token → 401")
			.get("/users")
			.check(status().is(401)));

	// Auth - Login (user)
	public static final ChainBuilder loginUser = feed(csv("data/users.csv").circular())
			.exec(http("Auth - Login (user)")
					.post("/auth/login")
					.body(StringBody("{\"email\":\"#{email}\",\"password\":\"#{password}\"}"))
					.check(status().is(200))
					.check(jmesPath("token").saveAs("userToken")));

	// Users - Delete (as non-admin → 403)
	public static final ChainBuilder deleteUserAsUser = exec(http("Users - Delete (403) - #{createdUserId}")
			.delete("/users/#{createdUserId}")
			.header("Authorization", "Bearer #{userToken}")
			.check(status().is(403)));

	// Users - Delete (as admin → 204)
	public static final ChainBuilder deleteUserAsAdmin = exec(http("Users - Delete (204) - #{createdUserId}")
			.delete("/users/#{createdUserId}")
			.header("Authorization", "Bearer #{adminToken}")
			.check(status().is(204)));

	// Auth - Login (short token 2s)
	public static final ChainBuilder loginAdminShortToken = feed(csv("data/admin.csv").circular())
			.exec(http("Auth - Login (short token 2s)")
					.post("/auth/login")
					.body(StringBody("{\"email\":\"#{email}\",\"password\":\"#{password}\",\"expOverrideSec\":2}"))
					.check(status().is(200))
					.check(jmesPath("token").saveAs("shortToken")));

	// Security - Expired token -> 401
	public static final ChainBuilder expiredToken = exec(http("Security - Expired token")
			.get("/users")
			.header("Authorization", "Bearer #{shortToken}")
			.check(status().is(401)));
}
