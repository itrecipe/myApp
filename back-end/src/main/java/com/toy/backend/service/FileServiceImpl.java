package com.toy.backend.service;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;
import com.toy.backend.domain.Files;
import com.toy.backend.mapper.FileMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileMapper fileMapper;

    @Value("${upload.path}")
    private String uploadPath;  // 업로드 경로

    @Override
    public List<Files> list() {
        return fileMapper.list();
    }

    @Override
    public Files select(Long no) {
        return fileMapper.select(no);
    }

    @Override
    public Files selectById(String id) {
        return fileMapper.selectById(id);
    }

    @Override
    public boolean insert(Files entity) {
        return fileMapper.insert(entity) > 0;
    }

    @Override
    public boolean update(Files entity) {
        return fileMapper.update(entity) > 0;
    }

    @Override
    public boolean updateById(Files entity) {
        return fileMapper.updateById(entity) > 0;
    }

   /* 초기 작성 코드 (delete, deleteById)
   - 해당 코드는 메퍼 인터페이스랑 연결만 해둔 상태
        @Override
        public boolean delete(Long no) {
            return fileMapper.delete(no) > 0;
        }

        @Override
        public boolean deleteById(String id) {
            return fileMapper.deleteById(id) > 0;
        }
    */

    @Override
    public boolean delete(Long no) {
        Files file = fileMapper.select(no);     // 파일 정보 조회
        delete(file);                           // 1. 파일 삭제
        return fileMapper.delete(no) > 0;       // 2. db 데이터 삭제
    }

    @Override
    public boolean deleteById(String id) {
        Files file = fileMapper.selectById(id);  // 파일 정보 조회
        delete(file);                            // 1. 파일 삭제
        return fileMapper.deleteById(id) > 0;    // 2. DB 데이터 삭제
    }

    // 파일 시스템의 파일 삭제 (공통)
    public boolean delete(Files file) {
        if( file == null ) return false;

        String filePath = file.getFilePath();

        File deleteFile = new File(filePath); // 파일 객체에 접근할 수 있는 객체 생성

        // 삭제된 파일이 존재하는지 체크
        if( !deleteFile.exists() ) {
            log.error("파일이 존재하지 않음!");
            return false;
        }

        // deleted 삭제 여부를 확인
        boolean deleted = deleteFile.delete();

        if( deleted ) {
            log.info("파일이 삭제 되었습니다.");
            log.info("- " + filePath);
        }
        return true;
    }

    @Override
    public List<Files> listByParent(Files file) {
        return fileMapper.listByParent(file);
    }

    /* 초기 작성 코드 (deleteByParent)
        @Override
        public int deleteByParent(Files file) {
            return fileMapper.deleteByParent(file);
        }
    */

    @Override
    public int deleteByParent(Files file) {
        List<Files> fileList = fileMapper.listByParent(file); // 삭제할 리스트를 조회

        for (Files deleteFile : fileList) {
            // 파일 삭제
            delete(deleteFile);
        }

        // DB 데이터 삭제
        return fileMapper.deleteByParent(file);
    }

    // 파일 업로드 (단건)
    @Override
    public int upload(Files file) throws Exception {

        int result = 0;
        MultipartFile multipartFile = file.getData();

        // 파일이 없을때
        if (multipartFile.isEmpty()) {
            return result;
        }

        // 1. FS (File System)에 등록 [파일 복사]
         /*
            - 파일 정보 : 원본파일명, 파일 용량, 파일 데이터
                        파일명, 파일경로
         */
        String originName = multipartFile.getOriginalFilename();
        long fileSize = multipartFile.getSize();
        byte[] fileData = multipartFile.getBytes();
        String fileName = UUID.randomUUID().toString() + "_" + originName;
        String filePath = uploadPath + "/" + fileName;
        File uploadFile = new File(filePath);
        FileCopyUtils.copy(fileData, uploadFile); // 파일 복사 (업로드)

        // 2. DB에 등록
        file.setOriginName(originName);
        file.setFileName(fileName);
        file.setFilePath(filePath);
        file.setFileSize(fileSize);

        result = fileMapper.insert(file);

        return result;
    }

    // 파일 업로드 (다중)
    @Override
    public int upload(List<Files> fileList) throws Exception {
        int result = 0;

        if (fileList == null || fileList.isEmpty()) // fileList가 null이거나 없는 경우 결과값 리턴
            return result;

        for (Files files : fileList) {
            result += upload(files);
        }
        return result;
    }

    // 파일 다운로드
    @Override
    public int download(String id, HttpServletResponse response) throws Exception {
        Files file = fileMapper.selectById(id); // 파일을 먼저 조회하기

        // 파일이 없을 경우 응답 상태 반환
        if (file == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return 0;
        }

        // 파일 입력
        String fileName = file.getOriginName(); // 파일명 (다운로드시 - 원본 파일명)
        String filePath = file.getFilePath(); // 파일 경로
        File downloadFile = new File(filePath);
        FileInputStream fis = new FileInputStream(downloadFile);

        /* 파일 다운로드 응답 헤더 세팅 (2가지)
            - ContentType : application/octet-stream
            - Content-Disposition : attachment, filename = "파일명.확장자"
        */
        fileName = URLEncoder.encode(fileName, "UTF-8"); // 한글 파일명 인코딩 처리
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        // 파일 출력
        ServletOutputStream sos = response.getOutputStream();

        // 다운로드
        int result = FileCopyUtils.copy(fis, sos);

        // 위 작업들이 성공적으로 수행된 경우 각각의 자원들을 해제
        fis.close();
        sos.close();

        return result;
    }

    /* 파일 선택 삭제 (no 값을 기준으로 삭제) : 서비스단 실제 구현체에서 삭제 처리 방법1
        @Override
        public boolean deleteFiles(List<Long> noList) {
            if( noList == null ) return false;

            // 1. 파일 삭제
            for( Long no : noList ) {
                Files file = select(no);
                delete(file);
                log.info("deleteFiles -> delete(file) : " + file);
            }
            // 2. 파일 데이터 삭제
            String nos = "";    // 1, 2, 3 -> output 형식
            for(int i = 0; i < noList.size(); i++) {

                // nos += (noList.get(i) + ""); // 기존 코드 (초기)
                // nos += (noList.get(i).toString()); 기존 코드(변형) - 1 : 굳이 괄호로 안 묶어도 변환처리가 된다길래 아래 코드로 변경
                // nos += noList.get(i).toString(); 기존 코드(변형) - 2 : 여기선 타입이 Long이라 문자열로 변환시켜줘야 해서 toString() 메소드가 필요

                nos += noList.get(i).toString();
                if( i != noList.size() - 1 )
                    nos += ",";
            }
            log.info("deleteFiles -> nos : " + nos);
            return fileMapper.deleteFiles(nos) > 0;
        }
    */

    // 파일 선택 삭제 : FileMapper.xml -> Mybatis의 <foreach>로 삭제 처리 하는 방법2 (구분자 처리 포함)
    @Override
    public boolean deleteFiles(List<Long> noList) {
        if( noList == null ) return false;

        // 1. 파일 삭제
        for( Long no : noList ) {
            Files file = select(no);
            delete(file);
            log.info("deleteFiles -> delete(file) : " + file);
        }

        // 2. 파일 데이터 삭제
        return fileMapper.deleteFileList(noList) > 0;
    }

    /* 파일 삭제 (id 값을 기준으로 삭제) : 서비스단 실제 구현체에서 삭제 처리 방법1
    @Override
    public boolean deleteFilesById(List<String> idList) {
        if( idList == null ) return false;

        // 1. 파일 삭제
        for( String id : idList ) {
            Files file = selectById(id);
            delete(file);
            log.info("deleteFilesById -> delete(file) : " + file);
        }
        // 2. 파일 데이터 삭제
        String ids = "";    // 'id1', 'id2', 'id3' <- output 형식
        for(int i = 0; i < idList.size(); i++) {
            // ids += idList.get(i); -> 이전 코드 (여기서는 타입이 String이기 때문에 toString() 필요없음)
            ids += ("'" + idList.get(i) + "'"); // 문자열로 정리가 되어야 해서 묶을 수 있도록 따옴표(구분자)를 추가
            if(i != idList.size() - 1)
                ids += ",";
        }
        log.info("deleteFilesById -> ids : " + ids);
        return fileMapper.deleteFilesById(ids) > 0;
    }
    */

    // 파일 삭제 : FileMapper.xml -> Mybatis의 <foreach>로 삭제 처리 하는 방법2 (구분자 처리 포함)
    @Override
    public boolean deleteFilesById(List<String> idList) {
        if( idList == null ) return false;

        // 1. 파일 삭제
        for( String id : idList ) {
            Files file = selectById(id);
            delete(file);
            log.info("deleteFileListById -> delete(file) : " + file);
        }
        // 2. 파일 데이터 삭제
        return fileMapper.deleteFileListById(idList) > 0;
    }
}