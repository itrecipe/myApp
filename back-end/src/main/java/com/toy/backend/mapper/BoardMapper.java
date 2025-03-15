package com.toy.backend.mapper;

import com.toy.backend.domain.Boards;
// import com.toy.backend.vo.BoardEntity;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BoardMapper extends BaseMapper<Boards> {
	
//	List<Boards> search(BoardEntity boardVO); // 보드 객체로 넘기는 방법
	
	List<Boards> search(@Param("searchType") String searchType, @Param("keyword") String keyword);

}
