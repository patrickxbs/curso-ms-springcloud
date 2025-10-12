package com.patrick.api_gateway.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public SwaggerUiConfigParameters swaggerUiConfigParameters(SwaggerUiConfigProperties swaggerUiConfigProperties) {
        return new SwaggerUiConfigParameters(swaggerUiConfigProperties);
    }

    @Bean
    @Lazy(false)
    public List<GroupedOpenApi> apis(SwaggerUiConfigParameters config, RouteDefinitionLocator locator) {

        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
        List<GroupedOpenApi> groups = new ArrayList<>();

        if (definitions != null) {
            definitions.stream()
                    .filter(routeDefinition -> routeDefinition.getId().matches(".*-service"))
                    .forEach(routeDefinition -> {
                        String name = routeDefinition.getId();
                        config.addGroup(name);
                        groups.add(GroupedOpenApi.builder()
                                .group(name)
                                .pathsToMatch("/" + name + "/**").build());
                    });
        }
        return groups;
    }
}
