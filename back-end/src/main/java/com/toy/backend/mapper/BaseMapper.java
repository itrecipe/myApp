package com.toy.backend.mapper;

/*
- @Mapper : 해당 인터페이스가 Mybatis에서 SQL을 실행하는 Mapper라는 것을 알려주는 애너테이션
- BaseMapper는 공통적인 crud 메서드들을 재사용하기 위해 만들어둔 서비스 인터페이스 메퍼 다른
 메퍼에서 재사용이 가능하며, 코드 유지보수가 용이하다.
*/

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface BaseMapper<E> {
    public List<E> list();
    public E select(Long no);
    public E selectById(String id);
    public int insert(E entity);
    public int update(E entity);
    public int updateById(E entity);
    public int delete(Long no);
    public int deleteById(String id);
}
