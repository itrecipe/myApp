package com.toy.backend.service;

import com.github.pagehelper.PageInfo;
import com.toy.backend.domain.Boards;

public interface BoardService extends BaseService<Boards> {

    // 페이징
    public PageInfo<Boards> list(int page, int size);
}
