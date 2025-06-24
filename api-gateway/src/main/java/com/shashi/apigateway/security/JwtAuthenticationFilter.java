package com.shashi.apigateway.security;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private static final Map<String, List<String>> roleAccessMap = Map.of(
            "/admin", List.of("ADMIN"),
            "/owner", List.of("OWNER"),
            "/user", List.of("USER", "DRIVER"),
            "/vehicles/user", List.of("USER", "DRIVER"),
            "/payments/user", List.of("USER", "DRIVER"),
            "/reservations/user", List.of("USER", "DRIVER"),
            "/public", List.of("ANY") // Optional: allow public APIs
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();

        // Bypass auth for public endpoints
        if (path.contains("/auth") || path.contains("/login") || path.contains("/register") || path.contains("/public")) {
            return chain.filter(exchange);
        }

        HttpHeaders headers = exchange.getRequest().getHeaders();
        if (!headers.containsKey(AUTHORIZATION)) {
            return this.onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
        }

        String token = headers.getFirst(AUTHORIZATION);
        if (token == null || !token.startsWith(BEARER)) {
            return this.onError(exchange, "Invalid Authorization Header", HttpStatus.UNAUTHORIZED);
        }

        token = token.replace(BEARER, "");
        if (!jwtUtil.isTokenValid(token)) {
            return this.onError(exchange, "Invalid or Expired JWT Token", HttpStatus.UNAUTHORIZED);
        }

        Claims claims = jwtUtil.getClaims(token);
        String userRole = claims.get("role", String.class);
        String userId = claims.getSubject();

        // Attach claims to request
        exchange.getRequest().mutate()
                .header("userId", userId)
                .header("role", userRole);

        // Check role authorization
        String[] pathParts = path.split("/", 4); // e.g., ["", "vehicle-service", "admin", "vehicles"]
        if (pathParts.length >= 3) {
            String accessKey = "/" + pathParts[2]; // e.g., "/admin"
            List<String> allowedRoles = roleAccessMap.get(accessKey);
            if (allowedRoles != null && !allowedRoles.contains(userRole)) {
                return this.onError(exchange, "Forbidden: Role " + userRole + " not allowed for " + accessKey, HttpStatus.FORBIDDEN);
            }
        }

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // High priority
    }
}
