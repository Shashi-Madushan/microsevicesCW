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

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();

        // Bypass auth for login/register endpoints
        if (path.contains("/auth") || path.contains("/login") || path.contains("/register")) {
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
        exchange.getRequest().mutate()
                .header("userId", claims.getSubject())
                .header("role", claims.get("role", String.class));

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
