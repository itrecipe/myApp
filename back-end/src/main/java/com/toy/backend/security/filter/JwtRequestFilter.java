package com.toy.backend.security.filter;

import com.toy.backend.security.constants.SecurityConstants;
import com.toy.backend.security.provider.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        String authorization = request.getHeader(SecurityConstants.TOKEN_HEADER); // 결국 얘가 Authorization
        log.info("authorization : " + authorization);

        /* String authorization = request.getHeader( SecurityConstants.TOKEN_PREFIX );

              request.getHeader()는 요청 헤더에서 값을 가져올 때, 헤더의 키(이름)를 사용한다.
              SecurityConstants.TOKEN_PREFIX는 보통 "Bearer " 값을 갖고 있기 때문에,
              이를 헤더 이름으로 잘못 사용하여 null을 반환하게 되는 문제가 발생 했고,
              결과적으로 authorization 값이 null로 처리되어 JWT 추출에 실패함.
         */

        /* "Bearer {JWT}" 형태로 토큰이 잘 들어오는지 체크하는 로직
           만약 헤더가 없거나, 길이가 0이거나, 접두사 "Bearer "로 들어오지 않는 경우
           즉, 올바르게 헤더 정보가 들어오지 않는 경우 다음 필터로 넘어가도록 처리하는 로직
         */
        if (authorization == null || authorization.length() == 0 || !authorization.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        /* JWT
           : "Bearer {jwt}" -> "Bearer " 제거 = JWT
           앞에 있는 Bearer를 떼어내고 JWT만 남도록 추출하는 작업
        */
        String jwt = authorization.replace(SecurityConstants.TOKEN_PREFIX, "");

        // 2. 인증 시도
        Authentication authentication = jwtProvider.getAuthenticationToken(jwt);

        if (authentication != null && authentication.isAuthenticated()) {
            log.info("JWT를 통한 인증 완료");
        }

        // 3. JWT 검증
        boolean result = jwtProvider.validateToken(jwt);

        if (result) {
            // JWT 토큰이 유효하면, 인증 처리 완료
            log.info("유효한 JWT 토큰");
            /* SecurityContextHolder  : 사용자 보안정보를 담는 객체
               Authentication         : 사용자 인증 정보

               사용자 보안정보를 담는 객체를 사용자 인증 정보에 넣어주는 작업이며
               결론은, 로그인 작업을 의미한다.
             */
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 4. 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}