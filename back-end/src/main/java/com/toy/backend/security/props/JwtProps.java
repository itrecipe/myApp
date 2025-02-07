package com.toy.backend.security.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/* 해당 클래스는 Spring Boot의 `@ConfigurationProperties`
   어노테이션을 사용하여, application.properties(속성 설정 파일)로 부터
   JWT 관련 프로퍼티를 관리하는 프로퍼티 클래스
*/
@Data
@Component
@ConfigurationProperties("com.toy.backend")
/* com.toy.back_end 까지가 베이스 패키지이며,
   해당 경로의 하위 속성들을 지정 한다.
*/
public class JwtProps {
    /* com.toy.back_end.secretKey로 지정된 프로퍼티 값을 주입 받는 필드
       com.toy.back_end.secret-key -> secretKey : {인코딩된 시크릿 키}
    */
    private String secretKey;
}