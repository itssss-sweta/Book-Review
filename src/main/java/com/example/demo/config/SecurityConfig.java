package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails userDetails = User.builder().username("user").password("{noop}password").roles("USER").build();
    return new InMemoryUserDetailsManager(userDetails);
  }

  // @Bean
  // public PasswordEncoder passwordEncoder() {
  // return new BCryptPasswordEncoder();
  // }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http)
      throws Exception {
    return http.cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            // .requestMatchers(HttpMethod.POST, "/api/auth/signup/**").permitAll()
            // .requestMatchers(HttpMethod.POST, "/api/auth/login/**").permitAll()
            // .requestMatchers(HttpMethod.GET, "/authentication-docs/**").permitAll()
            .anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults()).build();
  }

}
