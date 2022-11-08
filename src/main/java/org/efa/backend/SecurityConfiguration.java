package org.efa.backend;

import org.efa.backend.miscellaneous.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

//    @Bean
//    public PasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Autowired
//    private IUserBusiness userBusiness;
//
//
//    @Bean
//    public AuthenticationManager authenticationManager() {
//        return new CustomAuthenticationManager(bCryptPasswordEncoder(), userBusiness);
//    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("*").allowedHeaders("*").allowedOrigins("*");
            }
        };
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests().antMatchers("/**").permitAll()
                .anyRequest().authenticated();
        return http.build();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.cors().and().csrf().disable()
//                .authorizeRequests().antMatchers(HttpMethod.POST, Constants.URL_LOGIN).permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        return http.build();
//    }
}
