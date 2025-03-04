package com.toy.backend.mapper;

import com.toy.backend.domain.Files;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper extends BaseMapper<Files> {

    // 부모 기준 목록
    public List<Files> listByParent(Files file);

    // 부모 기준 삭제
    public int deleteByParent(Files file);

    // 선택 삭제 (no)
    public int deleteFiles(String noList);

    // 선택 삭제 (id)
    public int deleteFilesById(String idList);

}
