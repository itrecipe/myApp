package com.toy.backend.service;

import com.toy.backend.domain.Files;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public interface FileService extends BaseService<Files> {

    // 부모 기준 목록
    public List<Files> listByParent(Files file);

    // 부모 기준 삭제
    public int deleteByParent(Files file);

    // 파일 업로드 (단건)
    public int upload(Files file) throws Exception;

    // 파일 업로드 - 1 (다중처리)
    public int upload(List<Files> fileList) throws Exception;

    // 파일 다운로드
    public int download(String id, HttpServletResponse response) throws Exception;

    // 선택 삭제 (no 기준) -> Mybatis의 <foreach> 문법과 속성(기능)들을 사용하여 반복문을 돌려서 처리 하는 방법2
    public boolean deleteFiles(List<Long> noList);

    // 선택 삭제 (id 기준) -> Mybatis의 <foreach> 문법과 속성(기능)들을 사용하여 반복문을 돌려서 처리 하는 방법2
    public boolean deleteFilesById(List<String> idList);

    /* [유의 사항] - FileMapper 인터페이스 파일 참조
        FileMapper 인터페이스에서는 서비스단 실체 구현체에서 자바 반복문으로 처리하는 방법과
        FileMapper.xml 에 정의한 Mybatis 문법인 foreach 문법을 사용해서 처리 하는 방법
        2가지를 사용하고 있다. 그래서 FileMapper 인터페이스 구조와 서비스단 실제 구현체 구조가 살짝 다르다.

        메소드명을 공통으로 사용하고 있고 위 두가지 방식을 공용으로 모두 사용할 수 있도록 구성되어 있으며,
        컨트롤러에서도 메소드명을 공용으로 사용중이기 때문에 deleteFileList, deleteFileListById 메소드는
        따로 정의하지 않았다는 점을 유의할것.
    */
    
    // 타입별 파일 조회
    public Files selectByType(Files file);
    
    // 타입별 파일 목록
    public List<Files> listByType(Files file);

}