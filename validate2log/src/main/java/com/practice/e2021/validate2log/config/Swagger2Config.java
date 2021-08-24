//package com.practice.e2021.validate2log.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@Configuration
//@EnableSwagger2
//public class Swagger2Config {
//
//    @Bean
//    public Docket createRestApi() {
//
//        ApiInfo apiInfo = new ApiInfoBuilder()
//                .title("后台接口")
//                .description("这里描述内容")
//                .contact(new Contact("Scout", "", ""))
//                .termsOfServiceUrl("http://localhost:8081/")
//                .version("1.0")
//                .build();
//
//        return new Docket(DocumentationType.SWAGGER_2)
//                .host("http://localhost:8081/")
//                .groupName("后台接口")
//                .apiInfo(apiInfo)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.practice.e2021.validate2log.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//}