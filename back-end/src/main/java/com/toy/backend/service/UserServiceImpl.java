package com.toy.backend.service;

import com.toy.backend.domain.UserAuth;
import com.toy.backend.domain.Users;
import com.toy.backend.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder; // PasswordEncoder: 비밀번호 암호화를 위해 의존성 주입

    @Autowired
    private AuthenticationManager authenticationManager; // AuthenticationManager: 로그인 처리를 하기 위해 의존성 주입

    @Override
    public boolean insert(Users user) throws Exception {
        // 비밀번호 암호화
        String password = user.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        // 회원 등록
        int result = userMapper.join(user);

        // 권한 등록 (추가 및 개선중)
//        if(result > 0 && user.getAuthList() != null) {
//            for (UserAuth auth : user.getAuthList()) {
//                auth.setUsername(user.getUsername()); // 사용자명 설정
//                result += userMapper.insertAuth(auth);
//            }
//        }

        // 권한 등록
        if(result > 0) {
            UserAuth userAuth = UserAuth.builder()
                    .username(user.getUsername())
                    .auth("ROLE_USER")
                    .build();
            result += userMapper.insertAuth(userAuth);
        }

        return result > 0;
    }

    @Override
    public Users select(String username) throws Exception {
        return userMapper.select(username);
    }

    @Override
    public void login(Users user, HttpServletRequest request) throws Exception {

        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    @Override
    public boolean update(Users user) throws Exception {
        // 비밀번호 암호화
        String password = user.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        int result = userMapper.update(user);
        return result > 0;
    }

    @Override
    public boolean delete(String username) throws Exception {
        return userMapper.delete(username) > 0;
    }
}