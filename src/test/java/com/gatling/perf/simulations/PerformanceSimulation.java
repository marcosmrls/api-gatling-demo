package com.gatling.perf.simulations;

import com.gatling.perf.config.HttpProtocol;
import com.gatling.perf.feeders.DynamicUsersFeeder;
import com.gatling.perf.scenarios.CommonChains;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

import java.time.Duration;

public class PerformanceSimulation extends Simulation {

	private static final int USERS = Integer.parseInt(System.getProperty("USERS", "3"));
	private static final int RAMP_DURATION = Integer.parseInt(System.getProperty("RAMP_DURATION", "1"));
	private static final int CONCURRENT_DURATION = Integer.parseInt(System.getProperty("CONCURRENT_DURATION", "2"));
	private static final int TEST_DURATION = Integer.parseInt(System.getProperty("TEST_DURATION", "5"));

	@Override
	public void before() {
		System.out.printf("%nEjecutando prueba con %d Usuarios Virtuales%n", USERS);
		System.out.printf("Ramping de Subida de %d minutos%n", RAMP_DURATION);
		System.out.printf("%d usuarios Concurrentes durante %d minutos%n", USERS, CONCURRENT_DURATION);
		System.out.printf("Ramping de Bajada de %d minutos%n", RAMP_DURATION);
		System.out.printf("Tiempo total de prueba: %d minutos%n%n", TEST_DURATION);
	}

	ScenarioBuilder smoke = scenario("API Demo Performance - Gatling")
			.exec(CommonChains.health)
			.exec(CommonChains.loginAdmin).exitHereIfFailed().pause(Duration.ofMillis(500), Duration.ofMillis(1000))
			.feed(DynamicUsersFeeder.uniqueUsers())
			.exec(CommonChains.createUser).exitHereIfFailed().pause(Duration.ofMillis(500), Duration.ofMillis(1000))
			.exec(CommonChains.getUserById)
			.exec(CommonChains.missingToken)
			.exec(CommonChains.loginUser)
			.exec(CommonChains.deleteUserAsUser)
			.exec(CommonChains.deleteUserAsAdmin).pause(Duration.ofMillis(500), Duration.ofMillis(1000))
			.exec(CommonChains.loginAdminShortToken).pause(Duration.ofSeconds(3))
			.exec(CommonChains.expiredToken)
			.pace(Duration.ofSeconds(6), Duration.ofSeconds(8));

	public PerformanceSimulation() {

		setUp(smoke.injectClosed(
				rampConcurrentUsers(0).to(USERS).during(Duration.ofMinutes(RAMP_DURATION)),
				constantConcurrentUsers(USERS).during(Duration.ofMinutes(CONCURRENT_DURATION)),
				rampConcurrentUsers(USERS).to(0).during(Duration.ofMinutes(RAMP_DURATION))))
				.maxDuration(Duration.ofMinutes(TEST_DURATION))
				.protocols(HttpProtocol.httpProtocol)
				.assertions(
						global().successfulRequests().percent().gt(99.0),
						forAll().responseTime().percentile4().lt(1200));
	}

	@Override
	public void after() {
		System.out.println("**********************************************");
		System.out.println("*********   Prueba Completada   **************");
		System.out.println("**********************************************");
		System.out.println("\n\n");
	}
}
