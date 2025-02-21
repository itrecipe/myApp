package com.toy.backend.domain;

import lombok.Data;

import java.util.Date;

/* @Data : 자동으로 Getter, Setter, 생성자, toString(), equals() 같은 메서드를 만들어주는 도구,
   마치 자동차의 엔진이나 바퀴를 일일이 만들 필요없이 버튼 하나로 완성되는 것과 비슷하다.
*/
@Data
public class Boards {
    private Long no;
    private String id;
    private String writer;
    private String content;
    private Date createdAt;
    private Date updateAt;

    // 파일

    // 파일 정보

}