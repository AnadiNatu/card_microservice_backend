package com.microservice.admin_service.config;


import com.microservice.admin_service.interceptor.FeignJwtRequestInterceptor;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new FeignJwtRequestInterceptor();
    }

    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder(){
        return new ErrorDecoder.Default();
    }
}
