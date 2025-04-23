package cn.dyw.auth.demo.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文档配置
 *
 * @author dyw770
 * @since 2024-05-03
 */
@Configuration
public class OpenApiDocConfiguration {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("auth")
                        .description("auth")
                        .version("0.0.1"))
                .externalDocs(new ExternalDocumentation()
                        .description("auth")
                        .url("https://dyw770.top/"))
                .components(new Components()
                        .addSecuritySchemes("basicScheme",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .in(SecurityScheme.In.HEADER)
                                        .bearerFormat("JWT")
                        )
                )
                .addSecurityItem(new SecurityRequirement()
                        .addList("basicScheme"));
    }
}
