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

    // 선택 삭제 (no)
    public int deleteFiles(String noList);

    // 선택 삭제 (id)
    public int deleteFilesById(String idList);

}