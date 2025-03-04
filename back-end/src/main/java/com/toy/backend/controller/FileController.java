package com.toy.backend.controller;

import com.toy.backend.domain.Files;
import com.toy.backend.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/files") // 요청할 매핑 경로 "/files"
public class FileController {

    @Autowired private FileService fileService;

    @GetMapping()
    public ResponseEntity<?> getAllFile() {
        try {
            List<Files> list = fileService.list();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneFile(@PathVariable String id) {
        try {
            Files file = fileService.selectById(id);
            return new ResponseEntity<>(file, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<?> createFile(@RequestBody Files file) {
        try {
            boolean result = fileService.insert(file);
            if(result) {
                return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateFile(@RequestBody Files file) {
        try {
            boolean result = fileService.updateById(file);
            if(result) {
                return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroyFile(@PathVariable("id") String id) {
        try {
            boolean result = fileService.deleteById(id);
            if(result) {
                return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* [DELETE] /files
       { "idList" : ['id1', 'id2', 'id3'] }

       파일 선택 삭제 (input 형식)
       @param noList : { "noList" : [1, 2, 3] }
       @param noList : ?noList=1, 2, 3  -> 바디로 전달이 되는지 확인
       @param idList : { "idList" : ['id1', 'id2', 'id3'] }
       @param idList : idList='id1', 'id2', 'id3' -> 쿼리 스트링으로 전달이 되는지 확인
       @return
     */

    @DeleteMapping("")
    public ResponseEntity<?> deleteFiles(
        @RequestParam("noList") List<Long> noList,
        @RequestParam("idList") List<String> idList
    ) {
        log.info("noList[] : " + noList);
        log.info("idList[] : " + idList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /* 파일 다운로드
     */
}