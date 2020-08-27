package com.interworks.gateway;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

@EnableDiscoveryClient
@SpringBootApplication
public class ServiceAppApplication {
	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes()
//					.route("rest_payment", r -> r.path("/payment**")
//                    .uri("lb://payment-service"))
//					.route("rest_product", r -> r.path("/product**")
//		            .uri("lb://product-service"))
				.route(p -> p.path("/payment**").filters(f -> f.hystrix(c -> c.setName("rest_payment")
				.setFallbackUri("forward:/fallback")))
				.uri("lb://payment-service"))
				.route(p -> p.path("/product**").filters(f -> f.hystrix(c -> c.setName("rest_product")
				.setFallbackUri("forward:/fallback")))
				.uri("lb://product-service"))
				.build();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ServiceAppApplication.class, args);
	}

	@GetMapping("/fallback")
	public ResponseEntity<List<String>> fallback() {
		System.out.println("fallback enabled");
		HttpHeaders headers = new HttpHeaders();
		headers.add("fallback", "true");
		return ResponseEntity.ok().headers(headers).body(Collections.emptyList());
	}
}
