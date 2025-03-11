package com.toy.backend.service;

import com.toy.backend.domain.CustomUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.toy.backend.mapper.UserMapper;
import com.toy.backend.domain.Users;

/*
    UserDetailsService : 사용자 정보를 불러오는 인터페이스
    해당 인터페이스를 구현하여, 사용자 정보를 로드하는 방법을
    정의할 수 있다.
*/
@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("::::: UserDetailServiceImpl :::::");
        log.info("- 사용자 정의 및 인증을 위한 사용자 정보 조회");
        log.info("- user : " + username);

        Users user = null;

        try {
            // 사용자 정보 및 권한 조회
            user = userMapper.select(username);
            log.info("사용자 정보 : " + user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없음" + username);
        }

        // Customuser -> UserDetails
        CustomUser customUser = new CustomUser(user);
        return customUser;
    }
}