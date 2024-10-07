package com.keduit.dadog.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       http.formLogin()
        //view 에서 form 액션을 loginPage 의 Post 로
               .loginPage("/dadog/users/login")
               .defaultSuccessUrl("/dadog/main")
               .usernameParameter("userId")
               .failureUrl("/dadog/users/login/error")
               .and()
               .logout()
               .logoutRequestMatcher(new AntPathRequestMatcher("/dadog/users/logout"))
               .logoutSuccessUrl("/dadog/main");

       http.authorizeHttpRequests()
               .mvcMatchers("/", "/dadog/**","favicon.ico").permitAll()
               .mvcMatchers("**/add/**",  "**/update/**", "**/delete/**").authenticated()
               .mvcMatchers("/dadog/admin/**").hasRole("ADMIN")
               .anyRequest().permitAll();

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
