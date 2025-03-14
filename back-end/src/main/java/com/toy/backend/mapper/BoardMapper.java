package com.toy.backend.mapper;

import com.toy.backend.domain.Boards;
import com.toy.backend.vo.BoardEntity;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BoardMapper extends BaseMapper<Boards> {
	
	List<Boards> search(BoardEntity boardVO);
	List<Boards> search(@Param("searchType") String searchType, @Param("keyword") String keyword);

}
