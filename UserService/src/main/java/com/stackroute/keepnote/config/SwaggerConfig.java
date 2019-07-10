package com.stackroute.keepnote.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*As in this class we are implementing Swagger So annotate the class with @Configuration and 
 * @EnableSwagger2
 * 
 */

@EnableSwagger2
@Configuration
public class SwaggerConfig {

	/*
	 * Annotate this method with @Bean . This method will return an Object of
	 * Docket. This method will implement logic for swagger
	 */

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.stackroute.keepnote.controller"))
				.paths(PathSelectors.ant("/api/v1/*")).build().securitySchemes(Lists.newArrayList(apiKey()));
	}

	@Bean
	public SecurityConfiguration securityConfig() {
		return new SecurityConfiguration(null, null, null, null, "", ApiKeyVehicle.HEADER, "Authorization", "");
	}

	private ApiKey apiKey() {
		return new ApiKey("Authroization", "Authorization", "header");
	}
}
