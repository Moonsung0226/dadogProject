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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 비활성화 (필요에 따라 활성화 가능)
        http.csrf().disable();

        // 로그인 및 로그아웃 설정
        http.formLogin()
                .loginPage("/dadoc/users/login") // 사용자 정의 로그인 페이지
                .defaultSuccessUrl("/dadoc/main") // 로그인 성공 시 이동할 경로
                .usernameParameter("userId") // 로그인에 사용할 파라미터 이름
                .failureUrl("/dadoc/users/login/error") // 로그인 실패 시 이동할 경로
                .and()
                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/dadoc/users/logout")) // 로그아웃 URL
//                .logoutSuccessUrl("/dadoc/main") // 로그아웃 성공 시 이동할 경로
                .permitAll(); // 로그아웃은 인증 없이 가능


        http.authorizeRequests()
                .antMatchers("/", "/kakao/callback", "/css/**", "/js/**", "/favicon.ico", "/kakao_login_medium_narrow.png").permitAll() // 정적 리소스와 특정 경로 허용
                .antMatchers("/dadoc/**").permitAll() // `/dadoc/` 경로는 모두 허용
                .antMatchers("/**/add/**", "/**/update/**", "/**/delete/**").authenticated() // 수정, 추가, 삭제 관련 경로는 인증 필요
                .antMatchers("/dadoc/admin/**").hasRole("ADMIN") // 관리자 경로는 ADMIN 권한 필요
                .anyRequest().authenticated(); // 그 외의 모든 요청은 인증 필요


        // 예외 처리
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()); // 인증되지 않은 사용자에 대한 예외 처리

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 인코딩을 위한 BCrypt 사용
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()); // 정적 리소스 무시
    }
}