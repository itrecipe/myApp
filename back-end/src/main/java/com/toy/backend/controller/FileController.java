package com.toy.backend.controller;

import com.toy.backend.domain.Files;
import com.toy.backend.service.FileService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/files") // 요청할 매핑 경로 "/files"
public class FileController {

    @Autowired private FileService fileService;

    @Autowired ResourceLoader resourceLoader;

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
            if (result) {
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
            if (result) {
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
            if (result) {
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

    /* 파일 삭제 초기 코드 (value 값 넘기기 전)
        - 해당 코드는 idList, noList 값을 둘다 한번에 넘겨야 하는 방식이며, (id, no) 값 각각 처리 불가

        @DeleteMapping("")
        public ResponseEntity<?> deleteFiles(@RequestParam("noList") List<Long> noList, @RequestParam("idList") List<String> idList) {
            log.info("deleteFiles 메소드 -> noList[] : " + noList);
            log.info("deleteFiles 메소드 -> idList[] : " + idList);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    */

    // 파일 삭제시 idList만 받을지 noList만 받을지 각각 처리할 수 있도록 로직 변경1
    /*
        @DeleteMapping("")
        public ResponseEntity<?> deleteFiles(
                @RequestParam (value = "noList", required = false) List<Long> noList,
                @RequestParam (value = "idList", required = false)  List<String> idList
        ) {
            log.info("deleteFiles 메소드 -> noList[] : " + noList);
            log.info("deleteFiles 메소드 -> idList[] : " + idList);

            boolean result = false;

            // idList & noList null 값 체크
            if( noList != null ) {
                result = fileService.deleteFiles(noList);
            }
            if ( idList != null ) {
                result = fileService.deleteFilesById(idList);
            }
            if (result) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    */

    // 파일 삭제시 idList만 받을지 noList만 받을지 각각 처리할 수 있도록 로직 변경2
    @DeleteMapping("")
    public ResponseEntity<?> deleteFiles(
            @RequestParam (value = "noList", required = false) List<Long> noList,
            @RequestParam (value = "idList", required = false)  List<String> idList
    ) {
        log.info("deleteFiles 메소드 -> noList[] : " + noList);
        log.info("deleteFiles 메소드 -> idList[] : " + idList);

        boolean result = false;

        // idList & noList null 값 체크
        if( noList != null ) {
            result = fileService.deleteFiles(noList);
        }
        if ( idList != null ) {
            result = fileService.deleteFilesById(idList);
        }
        if (result) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /* 파일 다운로드
       @param id
       @param response
       @throws Exception
    */
    @GetMapping("/download/{id}")
    public void fileDownload(@PathVariable("id") String id, HttpServletResponse response) throws Exception {
        fileService.download(id, response);
        log.info("fileService.download 잘 타는지확인 : " + id, response);
    }

    /*
      썸네일 이미지
      @param id
      @throws IOException
    */
    @GetMapping("/img/{id}")
    public void thumbnailImg
        (
                @PathVariable("id") String id,
                HttpServletResponse response
        ) throws IOException {
        Files file = fileService.selectById(id); // 파일의 id를 조회

        String filePath = file != null ? file.getFilePath() : null;

        // File imgFile = new File(filePath); 초기 작성 코드
        File imgFile;

        Resource resource = resourceLoader.getResource("classpath:static/img/no-image.png");

        // 파일 경로가 null 또는 파일이 존재하지 않는 경우
        // if (filePath == null || !imgFile.exists()) {  초기 작성 코드
        if ( filePath == null || !(imgFile = new File(filePath) ).exists() ) {
            // filePath가 null이거나, 해당 경로에 파일이 존재하지 않으면 기본 이미지(no-image.png) 적용

            // no-image.png 적용
            imgFile = resource.getFile();
            filePath = imgFile.getPath();
        }

        // 확장자
        String ext = filePath.substring(filePath.lastIndexOf(".") + 1);
        String mimeType = MimeTypeUtils.parseMimeType("image/" + ext).toString();
        log.info("mimeType: " + mimeType);
        MediaType mediaType = MediaType.valueOf(mimeType);

        if( mediaType == null ) {
            // 이미지 타입이 아닌 경우
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            imgFile = resource.getFile();
        } else {
            // 이미지 타입인 경우
            response.setContentType(mediaType.toString());
        }
        // 이미지 응답 받기 (입력 받기)
        FileInputStream fis = new FileInputStream(imgFile);
        ServletOutputStream sos = response.getOutputStream();
        FileCopyUtils.copy(fis, sos);
    }
    
    /*	URL : /files/{pTable}/{pNo}?type={MAIN, SUB}
	     @param pTable	: boards
	     @param pNo		: 1
	     @param type 	: MAIN, SUB	...
	     @return
     */
    @GetMapping("/{pTable}/{pNo}")
    public ResponseEntity<?> getAllFile(
    		@PathVariable("pTable") String pTable,
    		@PathVariable("pNo") Long pNo,
    		@RequestParam(value = "type", required = false) String type
    ) {
        try {
        	Files file = new Files();
        	file.setPTable(pTable);
        	file.setPNo(pNo);
        	file.setType(type);
        	
        	// type이 없을때 -> 부모 기준 모든 파일 조회
        	if( type == null ) {
        		List<Files> list = fileService.listByParent(file);
        		return new ResponseEntity<>(list, HttpStatus.OK);
        	}
        	
        	// type이 "MAIN"일 경우 -> 메인 파일 1개 조회
        	if ( type.equals("MAIN") ) {
        		Files mainFile = fileService.selectByType(file);
        		return new ResponseEntity<>(mainFile, HttpStatus.OK);
        	}
        	// type 그외 타입일 경우 -> 타입별 여러 파일
        	else {
        		List<Files> list = fileService.listByType(file);
        		return new ResponseEntity<>(list, HttpStatus.OK);
        	}
        	
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}