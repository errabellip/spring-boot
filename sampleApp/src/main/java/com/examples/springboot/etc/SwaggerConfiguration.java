package com.examples.springboot.etc;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ConditionalOnProperty(name = {"swagger.ui.enabled"}, havingValue = "true")
@EnableSwagger2
public class SwaggerConfiguration {
	
	/*
	 * For swagger api enablement	
	 */
	
	@Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("examples")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/*"))
                .build();
    }
     
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("********** Example APPI SWAGGER DOCUMENTATION **********")
                .description("Example API DETAILS")
                .version("1.0")
                .build();
    }

}
