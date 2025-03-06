package com.toy.backend.mapper;

import com.toy.backend.domain.Files;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FileMapper extends BaseMapper<Files> {

    // 부모 기준 목록
    public List<Files> listByParent(Files file);

    // 부모 기준 삭제
    public int deleteByParent(Files file);

    // 선택 삭제 (no 기준) -> 서비스단 실제 구현체에서 자바 코드로 반복문 돌려서 처리하는 방법1
    public int deleteFiles(String noList);

    // 선택 삭제 (id 기준) -> 서비스단 실제 구현체에서 자바 코드로 반복문 돌려서 처리하는 방법1
    public int deleteFilesById(String idList);

    // 선택 삭제 (no 기준) -> Mybatis의 <foreach> 문법과 속성(기능)들을 사용하여 반복문 돌려서 처리 하는 방법2
    /* ex) public int deleteFilesById(String idList); -> 해당 코드 처럼 String이나 int 같은 기본 타입이거나
           매개 변수 값을 하나씩 넘기는 경우는 괜찮지만 컬렉션 같은 경우는 @Param 어노테이션을 사용해서 받는 값을 명확하게
           명시 해줘야 한다.(deleteFileListById도 동일하게 작성 해줘야 한다.)
    */
    // public int deleteFileList(List<Long> noList); -> 이전 코드
    public int deleteFileList(@Param("noList") List<Long> noList);

    // 선택 삭제 (id 기준) -> Mybatis의 <foreach> 문법과 속성(기능)들을 사용하여 반복문 돌려서 처리 하는 방법2
    // public int deleteFileListById(List<String> idList); -> 이전 코드
    public int deleteFileListById(@Param("idList") List<String> idList);
}
