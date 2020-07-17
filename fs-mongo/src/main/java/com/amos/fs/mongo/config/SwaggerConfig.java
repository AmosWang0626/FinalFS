package com.amos.fs.mongo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * PROJECT: FinalFS
 * DESCRIPTION: SwaggerConfig
 *
 * @author amos
 * @date 2019/6/2
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .globalResponses(HttpMethod.GET, new ArrayList<>())
                .globalResponses(HttpMethod.PUT, new ArrayList<>())
                .globalResponses(HttpMethod.POST, new ArrayList<>())
                .globalResponses(HttpMethod.DELETE, new ArrayList<>())
                .enable(true)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.amos.fs.mongo.web"))
                // .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .protocols(newHashSet("https", "http"));
    }

    /**
     * API 页面上半部分展示信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("FinalFS")
                .description("技术栈: Spring Boot|MongoDB|Docker|Swagger")
                .contact(new Contact("AmosWang0626", null, "daoyuan0626@gmail.com"))
                .version("1.0")
                .build();
    }

    @SafeVarargs
    private final <T> Set<T> newHashSet(T... ts) {
        if (ts.length > 0) {
            return new LinkedHashSet<>(Arrays.asList(ts));
        }
        return null;
    }

}
