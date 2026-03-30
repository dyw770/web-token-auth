package cn.dyw.engine.server.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
                .info(new Info().title("fast-api")
                        .description("fast-api")
                        .version("0.0.1"))
                .externalDocs(new ExternalDocumentation()
                        .description("fast-api")
                        .url("https://dyw770.top/"));
    }
}
