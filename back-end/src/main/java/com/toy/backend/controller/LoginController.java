package com.toy.backend.controller;

import com.toy.backend.domain.AuthenticationRequest;
import com.toy.backend.security.constants.SecurityConstants;
import com.toy.backend.security.props.JwtProps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys; // 새로운 방식으로 SecretKey 생성을 위한 클래스
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/*
    1. JWT 토큰 생성
    - 로그인 요청 -> 인증 -> JWT 토큰 생성

    2. JWT 토큰 해석
    - 인증 자원 요청 -> JWT 토큰 해석
*/
@Slf4j
@RestController
public class LoginController {
    @Autowired
    private JwtProps jwtProps; // secretKey
    /*
        로그인 요청
        - 사용자가 로그인 요청을 통해 인증 시, JWT 토큰을 생성한다.

        URL => [POST] - /login  POST방식으로 로그인 등록 요청

        요청 메시지 데이터 => body :
        {
            "username" : "user",
            "password" : "1234"
        }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authReq) {
        /* (@RequestBody AuthenticationRequest authReq)
            @RequestBody 애너테이션을 쓰면 클라이언트 측에서 보낸 JSON 형태를 객체로 바인딩해서 받을 수 있다.
            위 코드에선 AuthenticationRequest 객체로 바인딩해서 authReq 매개변수(파라미터)로 받아온다.
        */

        // id, password
        String username = authReq.getUsername();
        String password = authReq.getPassword();

        log.info("username : " + username);
        log.info("password : " + password);

        // 사용자 권한 정보 세팅
        List<String> roles = new ArrayList<String>();
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");

        // 서명에 사용할 키 생성
        String secretKey = jwtProps.getSecretKey();
        byte[] signingKey = secretKey.getBytes();

        log.info("secretKey : "  + secretKey);

        // JWT 토큰 생성
        /* 만료시간 : ms 단위
            - 5일: 1000 * 60 * 60 * 24 * 5

            - 계산 과정
            1000 → 1초는 1000ms
            60 → 60초는 1분
            60 → 60분은 1시간
            24 → 24시간은 1일
            5 → 5일

            1000(ms) * 60(초) * 60(분) * 24(시간) * 5(일) = 5일 (432,000,000ms)
            JWT 토큰 만료 시간 설정: 이런 방식으로 시간 값을 밀리초 단위로 변환해서 사용.
         */
        int day5 = 1000 * 60 * 60 * 24 * 5;
        String jwt = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), Jwts.SIG.HS512 )  // 알고리즘 설정(기본적인 암호화를 위한 셋팅)
                .header()                                                   // 헤더 설정
                    .add("typ", SecurityConstants.TOKEN_TYPE)            // 타입을 jwt 라고 지정한것 -> typ : "jwt"
                .and()                                                     // 페이로드 설정
                .claim("uid", username)                                 // 사용자 ID
                .claim("rol", roles)                                    // 권한 정보
                .expiration( new Date(System.currentTimeMillis() + day5) ) // 만료시간
                .compact();                                                // 토큰 생성

        log.info("jwt : " + jwt);

        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }
    /* JWT 토큰 해석 : 클라이언트에서 인증 요청 시 보낸 JWT 토큰을 서버에서 해석(검증) 하는 작업

        @param header
        @return
    */
    @GetMapping("/user")
    public ResponseEntity<?> user(@RequestHeader(name = "Authorization") String authorization) {
        // Authorization은 JWT 인증 정보를 담는 헤더

        log.info("Authorization : " + authorization);

        /*  Authorization : "Bearer " + (JWT TOKEN)
            Authorization은 관례적으로 Bearer라는 접두사를 붙이며,
            그 다음 JWT TOKEN이 들어온다.
         */

        String jwt = authorization.substring(7); // 7번째까지의 문자열은 잘리고, 8번째 부터 남는다.
        log.info("jwt : " + jwt);

        String secretKey = jwtProps.getSecretKey();
        byte[] signingKey = secretKey.getBytes();

        // JWT 토큰 해석 : JWT 토큰을 가져와서 사용자의 정보를 꺼내온다.
        Jws<Claims> parsedToken = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(signingKey))
                .build()
                .parseSignedClaims(jwt);

        // 페이로드안의 속성들을 접근할 수 있는 객체를 만듬
        String username = parsedToken.getPayload().get("uid").toString();
        log.info("username : " + username);

        // 권한 가져오기
        Object roles = parsedToken.getPayload().get("rol");
        List<String> roleList = (List<String>) roles;
        log.info("roles : " + roles);
        log.info("roleList : " + roleList);

        // 해석된 토큰 정보를 응답
        return new ResponseEntity<>(parsedToken.toString(), HttpStatus.OK);
    }
}