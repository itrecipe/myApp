package com.toy.backend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.toy.backend.domain.Boards;
import com.toy.backend.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired BoardMapper boardMapper; // 메퍼 의존성 주입

    @Override
    public List<Boards> list() {
        return boardMapper.list();
    }

    @Override
    public Boards select(Long no) {
        return boardMapper.select(no);
    }

    @Override
    public Boards selectById(String id) {
        return boardMapper.selectById(id);
    }


    @Override
    public boolean insert(Boards entity) {
        return boardMapper.insert(entity) > 0;
        /* insert부터 deleteById까지 1건 이라도 데이터가
          있으면 true 또는 false로 응답하도록 느슨한 연결을 해준다.
         */
    }

    @Override
    public boolean update(Boards entity) {
        return boardMapper.update(entity) > 0;
    }

    @Override
    public boolean updateById(Boards entity) {
        return boardMapper.updateById(entity) > 0;
    }

    @Override
    public boolean delete(Long no) {
        return boardMapper.delete(no) > 0;
    }

    @Override
    public boolean deleteById(String id) {
        return boardMapper.deleteById(id) > 0;
    }

    // 페이징 (페이지 리스트 출력)
    @Override
    public PageInfo<Boards> list(int page, int size) {
        PageHelper.startPage(page,size);
        List<Boards> list = boardMapper.list();
        PageInfo<Boards> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
}
