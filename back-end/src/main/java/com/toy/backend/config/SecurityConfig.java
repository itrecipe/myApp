package com.toy.backend.config;

import com.toy.backend.security.filter.JwtAuthenticationFilter;
import com.toy.backend.security.filter.JwtRequestFilter;
import com.toy.backend.security.provider.JwtProvider;
import com.toy.backend.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// TODO : deprecated 없애기 (version : before SpringSecurity 5.4 ⬇)
// @EnableWebSecurity
// public class SecurityConfig extends WebSecurityConfigurerAdapter {

// OK : (version : after SpringSecurity 5.4 이상)

// 현재 프로젝트에서는 SpringSecurity6 버전을 사용

/* SwaggerUI API 테스트 페이지를 접속하기 위해 임시 방편으로 설정한 코드

// 트러블 슈팅 : 아래 설정으로 Swagger UI 접근 차단 문제 해결 (formLogin, httpBasic 비활성화 필요)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui/index.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll() // Swagger 관련 URL 허용
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults()) // 기본 로그인 폼 사용
                .httpBasic(Customizer.withDefaults()); // 기본 HTTP 인증 추가 (Swagger에서 API 호출 시 필요할 수도 있음)

        return http.build();
    }
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity( prePostEnabled = true, securedEnabled = true )
/* prePostEnabled = true로 활성화 시키면
   @PreAuthorize 어노테이션 활성화가 가능하다. (사용 용도는 메서드 권한 관리)
 */
public class SecurityConfig {

    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    @Autowired
    private JwtProvider jwtProvider;

    /* 아래 구조로 AuthenticationManager Bean을 등록 한다.
       private AuthenticationManager authenticationManager;

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
            return authenticationManager;
        }
    */

    private AuthenticationManager authenticationManager;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
        return authenticationManager;
    }

    // OK : (version : after SpringSecurity 5.4 이상 6버전)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 폼 기반 로그인 비활성화
        http.formLogin(login ->login.disable());

        // HTTP 기본 인증 비활성화
        http.httpBasic(basic ->basic.disable());
        /* HTTP 기본 인증을 비활성화 시켜서 JWT로만 인증 처리를 하고,
           JWT 토큰을 생성하여 인증 작업을 처리하는 방식으로 구현
        */

        // CSRF(Cross-Site Request Forgery) 공격 방어 기능 비활성화
        http.csrf(csrf ->csrf.disable());

        /* 세션 관리 정책 설정: STATELESS로 설정하면 서버는 세션을 생성하지 않는다.
           세션을 사용하여 인증하지 않고, JWT만 사용하여 인증하기 때문에 현재 프로젝트에서는
           세션 방식을 사용하지 않고 JWT를 사용하기 때문에 여기서는 필요 없다.
         */
        http.sessionManagement(management ->management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) );

        // 사용자 정의 인증 설정
        http.userDetailsService(userDetailServiceImpl); // 상단에 의존성 주입을 해둬야 설정이 가능하다. (기억할것!)

        /* 필터 설정
           - JWT 요청 필터 설정 1
           - JWT 인증 필터 설정 2
        */
        http.addFilterAt( new JwtAuthenticationFilter(authenticationManager, jwtProvider)
                         ,UsernamePasswordAuthenticationFilter.class )
                        .addFilterBefore(new JwtRequestFilter(authenticationManager, jwtProvider)
                        , UsernamePasswordAuthenticationFilter.class );

        // 구성이 완료된 SecurityFilterChain을 반환 합니다.
        return http.build();
    }

    // 비밀번호 암호화 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* TODO : deprecated 없애기 (version : before SpringSecurity 5.4 이하 버전)

         @Override
         protected void configure(HttpSecurity http) throws Exception {
            // 폼 기반 로그인 비활성화
            http.formLogin().disable()

            // HTTP 기본 인증 비활성화
            .httpBasic().disable();

            // CSRF(Cross-Site Request Forgery) 공격 방어 기능 비활성화
            http.csrf().disable();

            // 세션 관리 정책 설정: STATELESS로 설정하면 서버는 세션을 생성하지 않음
            // 세션을 사용하여 인증하지 않고, JWT 를 사용하여 인증하기 때문에, 세션 불필요
            http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
         }
     */
}