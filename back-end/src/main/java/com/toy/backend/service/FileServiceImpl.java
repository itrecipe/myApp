package com.toy.backend.service;

import java.io.File;
import java.util.List;
import java.util.UUID;
import com.toy.backend.domain.Files;
import com.toy.backend.mapper.FileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

  /*  기존 초기 코드 (샘플)
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
        Files file = fileMapper.selectById(id);     // 파일 정보 조회
        delete(file);                               // 1. 파일 삭제
        return fileMapper.deleteById(id) > 0;       // 2. DB 데이터 삭제
    }

    @Override
    public List<Files> listByParent(Files file) {
        return fileMapper.listByParent(file);
    }

    @Override
    public int deleteByParent(Files file) {
        List<Files> fileList = fileMapper.listByParent(file);

        for(Files deleteFile : fileList) {
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
        if( multipartFile.isEmpty() ) {
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

        if( fileList == null || fileList.isEmpty() ) // fileList가 null이거나 없는 경우 결과값 리턴
            return result;

        for (Files files : fileList) {
            result += upload(files);
        }
        return result;
    }

    // 파일 시스템의 파일 삭제
    public boolean delete(Files file) {
        if( file == null ) return false;

        String filePath = file.getFilePath();
        File deleteFile = new File(filePath); // 파일 객체에 접근할 수 있는 객체 생성

        if( !deleteFile.exists() ) {
            log.error("파일이 존재하지 않음!");
            return false;
        }

        boolean deleted = deleteFile.delete();
        if( deleted ) {
            log.info("파일이 삭제 되었습니다.");
            log.info("- " + filePath);
        }
        return true;
    }
}
