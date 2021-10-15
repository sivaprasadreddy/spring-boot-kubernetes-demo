package com.sivalabs.bookmarks.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI getOpenApiDocumentation() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("BookmarkService API")
                                .description("Description of BookmarkService API.")
                                .version("0.0.1")
                                .contact(
                                        new Contact()
                                                .name("Siva")
                                                .url("https://sivalabs.in")
                                                .email("siva@sivalabs.in"))
                                .termsOfService("TermsOfService")
                                .license(
                                        new License()
                                                .name("LICENSE")
                                                .url("https://license-url.com")))
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Documentation")
                                .url("https://sivalabs.in/bookmark-service"));
    }
}
