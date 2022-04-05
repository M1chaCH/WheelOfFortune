package ch.bbbaden.m151.wheeloffortune.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocsConfig {
    public static final String API_TITLE = "Wheel Of Fortune API";
    public static final String API_DESCRIPTION = "A School project, created for the Module 151";
    public static final String API_VERSION = "v1.0";

    @Bean
    public OpenAPI springShopOpenAPI () {
        return new OpenAPI()
                .info(new Info()
                        .title(API_TITLE)
                        .description(API_DESCRIPTION)
                        .version(API_VERSION));
    }
}
