package com.shuai.config;

import com.shuai.handler.LoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 处理CORS报错
 * 解决前后端交互的跨域问题
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 解决跨域问题
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET","POST","PUT","HEAD","OPTIONS","DELETE")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }

    //配置OrderedHiddenHttpMethodFilter
    @Bean
    public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter(){
        return new OrderedHiddenHttpMethodFilter();
    }


//    @Autowired
//    private LoginHandler loginHandler;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginHandler);
//    }
}
