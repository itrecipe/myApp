package com.toy.backend.security.filter;

import com.toy.backend.security.constants.SecurityConstants;
import com.toy.backend.security.provider.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public JwtRequestFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    /* 요청 필터 작업
       1. JWT 추출

       2. 인증 시도

       3. JWT 검증
          - 토큰이 유효하면, 인증 처리 완료
          - 토큰이 만료되면 별다른 작업 없이 넘어가도록 처리

       4. 다음 필터로 진행
    */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. JWT 추출
        String authorization = request.getHeader(SecurityConstants.TOKEN_PREFIX); // 결국 얘가 Authorization
        log.info("authorization : " + authorization);


        /* "Bearer {JWT}" 형태로 토큰이 잘 들어오는지 체크하는 로직
           만약 헤더가 없거나, 길이가 0이거나, 접두사 "Bearer "로 들어오지 않는 경우
           즉, 올바르게 헤더 정보가 들어오지 않는 경우 다음 필터로 넘어가도록 처리하는 로직
         */
        if( authorization == null || authorization.length() == 0 || !authorization.startsWith( SecurityConstants.TOKEN_PREFIX) ) {
            filterChain.doFilter(request, response);

            return ;
        }

    }
}
