package com.toy.backend.security.provider;

import com.toy.backend.domain.CustomUser;
import com.toy.backend.domain.UserAuth;
import com.toy.backend.domain.Users;
import com.toy.backend.mapper.UserMapper;
import com.toy.backend.security.constants.SecurityConstants;
import com.toy.backend.security.props.JwtProps;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/* JWT 토큰 관련 기능을 제공하는 클래스
    1. 토큰 생성
    2. 토큰 해석
    3. 토큰 검증
*/
@Slf4j
@Component
public class JwtProvider {

    @Autowired
    private JwtProps jwtProps;

    @Autowired
    private UserMapper userMapper;

    /* 1. JWT 토큰 생성
       사용자의 권한을 Provider에 넘겨주면 토큰을 생성한다.

       @param id
       @param username
       @param roles
       @return
     */
    public String createToken(String id, String username, List<String> roles) {

        SecretKey shaKey = getShaKey();

        int exp = 1000 * 60 * 60 * 24 * 5; // 5일 만드는 공식

        // JWT 토큰 생성
        String jwt = Jwts.builder().signWith(shaKey, Jwts.SIG.HS512)            // 시그니처 비밀키, 알고리즘 설정
                .header().add("typ", SecurityConstants.TOKEN_TYPE) // typ: jwt
                .and().expiration(new Date(System.currentTimeMillis() + exp)) // 토큰만료시간설정 (5일)
                .claim("id", id)                        // id       : 사용자 식별키
                .claim("username", username)            // username : 사용자 아이디
                .claim("rol", roles)                    // rol      : 회원 권한 목록
                .compact();

        /*
            트러블 슈팅 : 메서드 체이닝 중 .header() 밑에 .add()에 토큰 타입을 추가한뒤,
                        .and()를 빼먹으면서 .expiration() 메서드 연결이 안되는 문제가
                        발생했다. 검색 해보니 해당 문제는 .and()를 추가 해주면 해결된다.

        String jwt = Jwts.builder()
                            .signWith(shaKey, Jwts.SIG.HS512) // 시그니처 비밀키, 알고리즘 설정
                            .header()
                            .add("typ", SecurityConstants.TOKEN_TYPE) // typ: jwt
                            .expiration( new Date( System.currentTimeMillis() + exp ) ) // 토큰만료시간설정 (5일)
                            .claim("id", id)                                          // id : 사용자 식별키
                            .claim("username", username)                              // username : 사용자 ID
                            .claim("rol", roles)                                      // rol : 회원 권한 목록
                            .compact();
        */
        log.info("jwt : " + jwt);

        return jwt;
    }

    /*
        2. JWT 토큰 해석 : JWT 토큰이 들어오면
           인증 처리를 하기 위한 토큰 정보로 만드는 작업

       @param authorization
       @return
    */

    // 인증 처리를 하기 위한 토큰 해석 메서드
    public UsernamePasswordAuthenticationToken getAuthenticationToken(String authorization) {
         /*
            Authorization 헤더 값이 없거나 비어 있을 경우, 인증 중단
         */
        if (authorization == null || authorization.length() == 0)
            return null;

        /* Authorization 헤더 토큰 형태 : "Bearer {jwt}"
           정확히 문자열이 넘어 왔을때 인증 처리가 진행될 로직
        */
        try {
            // JWT에서 사용자 정보 추출 : 접두사인 Bearer를 떼어내는 작업
            String jwt = authorization.replace("Bearer ", "");
            log.info(" jwt : " + jwt);

            SecretKey shaKey = getShaKey();

            // JWT 토큰을 사용자 정보로 파싱(해석) 처리 하는 로직
            Jws<Claims> parsedToken = Jwts.parser()
                                            .verifyWith(shaKey)
                                            .build()
                                            .parseSignedClaims(jwt);

            log.info("parsedToken : " + parsedToken);

            /* 필요한 정보를 꺼내는 작업 */

            // 사용자 식별키(id)
            String id = parsedToken.getPayload().get("id").toString();
            // 사용자 아이디
            String username = parsedToken.getPayload().get("username").toString();
            // 회원 권한
            Object roles = parsedToken.getPayload().get("rol");

            Users user = new Users();
            user.setId(id);
            user.setUsername(username);

            // 권한 목록을 UserAuth 객체 리스트로 변환
            List<UserAuth> authList = ((List<?>) roles)
                    .stream()
                    .map(auth -> UserAuth.builder()
                            .username(username)
                            .auth(auth.toString())
                            .build()
                    )
                    .collect(Collectors.toList());
            user.setAuthList(authList);

            // 시큐리티 권한 목록을 SimpleGrantedAuthority 리스트로 변환
            List<SimpleGrantedAuthority> authorities
                    = ((List<?>) roles)
                    .stream()
                    .map(auth -> new SimpleGrantedAuthority(auth.toString()) )
                    .collect(Collectors.toList());

            // 추가 유저 정보 가져오기
            try {
                Users userInfo = userMapper.select(username);
                if (userInfo != null) {
                    user.setName(userInfo.getName());
                    user.setEmail(userInfo.getEmail());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error("토큰 해석 중, 회원 추가 정보 조회시 에러 발생! ");
            }

            // UserDetails 구현체 생성
            UserDetails userDetails = new CustomUser(user);

            // new UsernamePasswordAuthenticationToken( 사용자정보객체, 비밀번호, 권한목록 )
            return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
              /*
                여기서 user의 authList를 직접 사용할 수 없는 이유: 구조가 다르기 때문,
                UsernamePasswordAuthenticationToken 생성자에는 시큐리티 권한 목록이 필요하므로,
                위에서 생성한 authorities (SimpleGrantedAuthority 리스트) 를 사용해야 함.
             */

        // 변환과정에서 나올법한 각각의 예외들을 처리
        } catch (ExpiredJwtException exception) { // 만료된 토큰에 대한 예외처리
            log.warn("Request to parse expired JWT : {} failed : {}", authorization, exception.getMessage());
        } catch (UnsupportedJwtException exception) { // 지원하지 않는 토큰에 대한 예외처리
            log.warn("Request to parse unsupported JWT : {} failed : {}", authorization, exception.getMessage());
        } catch (MalformedJwtException exception) { // 기능적인 문제가 있는 경우의 대한 예외처리
            log.warn("Request to parse invalid JWT : {} failed : {}", authorization, exception.getMessage());
        } catch (IllegalArgumentException exception) { // 문법적으로 올바르지 않은 경우의 대한 예외처리
            log.warn("Request to parse empty or null JWT : {} failed : {}", authorization, exception.getMessage());
        }

        return null;
    }
    /*
        "secret-key" -> byte[] -> SecretKey
        - 문자열로 된 "secret-key"를 byte[]로 변환 하고
          바이트 코드로 변환된 정보를 가지고 객체 형태로 사용할 수
          있도록 처리 해주는 로직

         @return
     */

    /* 3. JWT 토큰 검증
        @param jwt
        @return
    */
    public boolean validateToken(String jwt) {
        try {
            // 토큰을 가져와서 변환 처리 하는 로직
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(getShaKey())
                    .build()
                    .parseSignedClaims(jwt);

            // 만료기한 추출
            Date expiration = claims.getPayload().getExpiration();
            log.info("토큰 만료기간 : " + expiration.toString());

            /* 날짜A.after( 날짜B )
              : 날짜A가 날짜B 보다 더 위에 있으면 true
            */
            boolean result = expiration.after( new Date() );
            return result;

        } catch (ExpiredJwtException e) {
            log.error("토큰 만료");
        } catch (JwtException e) {
            log.error("토큰 손상");
        } catch (NullPointerException e) {
            log.error("토큰 없음");
        } catch (Exception e) {
            log.error("토큰 검증 시 예외");
        }
        return false;
    }

    /* getShaKey() : 시그니처 검증을 위해 사용되는 객체
       getShaKey()는 여러곳에서 자주 쓰이기 때문에 따로
       메서드를 생성하였으며, 메소드를 호출하는 방식으로
       사용 및 관리 한다.
     */
    public SecretKey getShaKey() {
        String secretKey = jwtProps.getSecretKey();
        byte[] signingKey = secretKey.getBytes();
        SecretKey shaKey = Keys.hmacShaKeyFor(signingKey);

        return shaKey;
    }
}