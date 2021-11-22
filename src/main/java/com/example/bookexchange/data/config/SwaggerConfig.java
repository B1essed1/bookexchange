package com.example.bookexchange.data.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    private static final ApiInfo DEFAULT_API_INFO = new ApiInfo("Bokare", "Bookare Documentation" , "1.0",
            "terms", "DEFAULT_CONTACT", "APACHE 2.0" , "url");
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(DEFAULT_API_INFO);
    }

}
