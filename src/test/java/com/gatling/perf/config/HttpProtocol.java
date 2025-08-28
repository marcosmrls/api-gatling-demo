package com.gatling.perf.config;

import io.gatling.javaapi.http.*;

public class HttpProtocol {
	public static final HttpProtocolBuilder httpProtocol = HttpDsl.http
			.baseUrl(System.getProperty("baseUrl", "http://localhost:3000"))
			.acceptHeader("application/json")
			.contentTypeHeader("application/json");
}
