package com.seeder.apigateway.filter;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
@Slf4j
@NoArgsConstructor
public class RouteValidator {

  public static final List<String> openApiEndpoints = List.of(
    " /api/v1/users/"
  );

  public final Predicate<ServerHttpRequest> isSecured = request ->
    openApiEndpoints
      .stream()
      .noneMatch(uri -> request.getURI().getPath().equals(uri)) &&
    !request.getQueryParams().containsKey("email");
}
