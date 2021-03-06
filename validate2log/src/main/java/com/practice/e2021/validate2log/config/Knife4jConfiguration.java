package com.practice.e2021.validate2log.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //.title("swagger-bootstrap-ui-demo RESTful APIs")
                        .description("RESTful APIs")
                        .termsOfServiceUrl("http://www.xx.com/")
                        .contact(new Contact("libai", "https://github.com/libai", "libai@gmail.com"))
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("validate2log")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.practice.e2021.validate2log.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    public static int testRetry(int tryNum) {
        if (tryNum > 0) {
            System.out.println("当前:" + tryNum);
            return testRetry(--tryNum);
        }
        return tryNum;
    }

    public static void main(String[] args) {
        testRetry(3);
    }
}