package com.example.secure_password_vault.config.filter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitingFilter implements Filter {

	private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		String ip = httpRequest.getRemoteAddr();
		
		Bucket bucket = buckets.computeIfAbsent(ip, this::createBucket);
		
		if(bucket.tryConsume(1)) {
			chain.doFilter(request, response);
		}else {
			 HttpServletResponse httpResponse = (HttpServletResponse) response;
			 httpResponse.setStatus(429); //Too many requests
			 httpResponse.setContentType("application/json");
			 long nanosToWait = bucket.estimateAbilityToConsume(1).getNanosToWaitForRefill();
		     long secondsToWait = Duration.ofNanos(nanosToWait).getSeconds();

		     String json = String.format(
		                "{\"message\":\"Muitas requisições. Tente novamente em %d segundo%s.\", \"retry_after\": %d}",
		                secondsToWait,
		                secondsToWait != 1 ? "s" : "",
		                secondsToWait
		            );

		     httpResponse.getWriter().write(json);
		}
	}
	
	private Bucket createBucket(String ip) {
		Refill refill = Refill.greedy(15, Duration.ofMinutes(1));
		Bandwidth limit = Bandwidth.classic(15, refill);
		return Bucket.builder().addLimit(limit).build();
	}
}
