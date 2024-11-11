package ru.siladimary.BankProject.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mini bank API")
                        .version("1.0.0")
                        .description("API documentation for Mini bank application")
                );
    }
}
//http://localhost:8080/swagger-ui.html