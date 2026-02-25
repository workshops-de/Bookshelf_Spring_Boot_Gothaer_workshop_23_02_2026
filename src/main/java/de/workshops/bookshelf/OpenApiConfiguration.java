package de.workshops.bookshelf;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@Profile("test")
public class OpenApiConfiguration {

    @Bean
    public OpenAPI api(OpenApiConfigurationProperties properties) {
        return new OpenAPI()
            .info(
                new Info()
                    .title(properties.getTitle())
                    .version(properties.getVersion())
                    .license(new License()
                        .name(properties.getLicense().getName())
                        .url(properties.getLicense().getUrl().toString())
                    )
                    .description("API for managing a bookshelf up to %d books.".formatted(properties.getCapacity()))
            );
    }
}