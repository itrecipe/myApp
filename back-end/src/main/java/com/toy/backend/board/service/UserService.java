package com.toy.backend.board.service;

import com.toy.backend.board.domain.Users;

public interface UserService {

    // 회원 등록
    public boolean insert(Users users) throws Exception;

    // 회원 조회
    public Users select(String username) throws Exception;

    // 로그인
    public void login(String username) throws Exception;

    // 회원 수정
    public boolean update(Users user) throws Exception;

    // 회원 삭제
    public boolean delete(String username) throws Exception;
}
