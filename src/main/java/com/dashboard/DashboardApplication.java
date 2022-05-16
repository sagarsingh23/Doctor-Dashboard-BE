package com.dashboard;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class DashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(DashboardApplication.class, args);         //NOSONAR

	}

	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("DoctorDashBoard API")
				.description("DoctorDashBoard API for developers")
				.termsOfServiceUrl("http://localhost:3000/")
				.licenseUrl("DoctorDashBoard09@gmial.com").version("1.0").build();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
