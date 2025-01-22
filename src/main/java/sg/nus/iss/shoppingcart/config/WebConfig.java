package sg.nus.iss.shoppingcart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName WebConfig
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2024/10/5
 * @Version 1.0
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // allow all of paths
                .allowedOrigins("http://localhost:3000")  // allow the request from front-end
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // all the methods
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

