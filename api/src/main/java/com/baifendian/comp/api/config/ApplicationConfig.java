package com.baifendian.comp.api.config;

import com.baifendian.comp.api.interceptor.LanguageInterceptor;
import com.baifendian.comp.api.interceptor.SessionInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import javax.servlet.Filter;

@Configuration
@PropertySource({"classpath:application.yml"})
public class ApplicationConfig extends WebMvcConfigurerAdapter {

  private static boolean isRunning = false;

  public static boolean isRunning(){
    return isRunning;
  }

  @Value("${systemmanager.config.isRun}")
  public void systemRunning(String running){
    isRunning = Boolean.parseBoolean(running);
  }
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // registry.addInterceptor(loginInterceptor()).addPathPatterns("/*")
        /* .excludePathPatterns("/login") */
    registry.addInterceptor(sessionInterceptor()).addPathPatterns("/**/*");
    registry.addInterceptor(languageInterceptor()).addPathPatterns("/**/*");
  }

  @Bean
  public SessionInterceptor sessionInterceptor() {
    return new SessionInterceptor();
  }

  @Bean
  public LanguageInterceptor languageInterceptor(){
    return new LanguageInterceptor();
  }
//
//  @Override
//  public void addCorsMappings(CorsRegistry registry) {
//    registry.addMapping("/**").allowedOrigins("*");
//  }

  @Bean
  public Filter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    config.addExposedHeader("x-auth-token");
    config.addExposedHeader("x-total-count");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
}
