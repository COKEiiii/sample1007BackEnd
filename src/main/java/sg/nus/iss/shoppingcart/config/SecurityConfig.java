package sg.nus.iss.shoppingcart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @ClassName SecurityConfig
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2024/10/2
 * @Version 1.0
 */

@Configuration
public class SecurityConfig {
    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http
    //             .csrf(csrf -> csrf.disable())
    //             .authorizeHttpRequests(authz -> authz
    //                     .requestMatchers("/users/register", "/users/login").permitAll()
    //                     .anyRequest().authenticated()
    //             )
    //             .formLogin(form -> form
    //                     .loginPage("/signin")
    //                     .permitAll()
    //             )
    //             .logout(logout -> logout
    //                     .permitAll()
    //                     .logoutUrl("/users/logout")
    //                     .invalidateHttpSession(true)
    //                     .deleteCookies("JSESSIONID")
    //             )
    //             .sessionManagement(session -> session
    //                     .sessionFixation().migrateSession()
    //                     .maximumSessions(1)
    //             );
    //
    //     return http.build();
    // }

    // Define BCryptPasswordEncoder Bean
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
