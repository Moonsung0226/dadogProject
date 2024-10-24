package com.keduit.dadog.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 인코딩을 위한 BCrypt 사용
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 보호 설정
        http.csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        // 로그인 및 로그아웃 설정
        http.formLogin()
                .loginPage("/dadog/members/login") // 사용자 정의 로그인 페이지
                .defaultSuccessUrl("/dadog/main",true) // 로그인 성공 시 이동할 경로
                .usernameParameter("userId") // 로그인에 사용할 파라미터 이름
                .passwordParameter("password") // 비밀번호 파라미터 이름
                .failureUrl("/dadog/members/login/error") // 로그인 실패 시 이동할 경로
                .permitAll(); // 로그인 페이지는 인증 없이 접근 가능

        // 로그아웃 설정
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/dadog/main")
                .addLogoutHandler((request, response, authentication) -> {
                    request.getSession().setAttribute("message", "로그아웃되었습니다.");
                })
                .permitAll();

        // 요청 권한 설정
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "**/add").authenticated() // GET 요청 시 인증 필요
                .antMatchers(HttpMethod.GET, "**/delete/**").authenticated() // GET 요청 시 인증 필요
                .antMatchers(HttpMethod.GET, "**/update/**").authenticated() // GET 요청 시 인증 필요
                .antMatchers("/", "/kakao/callback", "/css/**", "/js/**", "/favicon.ico", "/kakao_login_medium_narrow.png").permitAll() // 정적 리소스와 특정 경로 허용
                .antMatchers("**/add", "**/delete/**", "**/update/").authenticated()
                .antMatchers("/img/**" ,"/dadog/lost/list","/dadog/lost/list/**","/dadog/lost/get/**",
                        "/dadog/adopt/list","/dadog/adopt/list/**","/dadog/adopt/**",
                        "/dadog/protect/list","/dadog/protect/list/**","/dadog/protect/get/**",
                        "/dadog/shelter","/dadog/shelter/**","/dadog/members/**","/dadog/spon","/dadog/boards","/dadog/boards/get/**").permitAll()
                .antMatchers("/dadog/admin/adopt/updateApi","/dadog/admin/adopt/api","/dadog/admin/**").hasRole("ADMIN") // 관리자 경로는 ADMIN 권한 필요
                .anyRequest().authenticated(); // 그 외의 모든 요청은 인증 필요

        // 예외 처리
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()); // 인증되지 않은 사용자에 대한 예외 처리

        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()); // 정적 리소스 무시
    }

    @Bean public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDenieHandler();
    }
}
