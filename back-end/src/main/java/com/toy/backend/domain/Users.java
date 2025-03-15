package com.toy.backend.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class Users {
	
    private Long no;
    private String id;
    private String username;
    private String password;
    private String name;
    private String email;
    private Date createdAt;
    private Date updatedAt;
    private Boolean enabled;

    private List<UserAuth> authList;

    // db에 uid 값이 들어가야하기 떄문에 같이 셋팅 한다.
    public Users() {
        this.id = UUID.randomUUID().toString();
    }
}
