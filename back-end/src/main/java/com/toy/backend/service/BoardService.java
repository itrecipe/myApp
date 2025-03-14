package com.toy.backend.service;

import com.github.pagehelper.PageInfo;
import com.toy.backend.domain.Boards;
import com.toy.backend.vo.BoardEntity;

public interface BoardService extends BaseService<Boards> {

    // 페이징 (게시판-페이지네이션)
    // public PageInfo<Boards> list(int page, int size);
    
	// 검색 적용
    public PageInfo<Boards> list(int page, int size, String searchType, String keyword);

	// public PageInfo<Boards> list(BoardEntity boardVO);
	
	
}
