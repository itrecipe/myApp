package com.toy.backend.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.toy.backend.domain.Boards;
import com.toy.backend.domain.Files;
import com.toy.backend.domain.Pagination;
import com.toy.backend.service.BoardService;
import com.toy.backend.service.FileService;
import com.toy.backend.vo.BoardEntity;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin("*") // *은 전체 경로 지정을 의미
@RestController
@RequestMapping("/boards")
public class BoardController {

    @Autowired BoardService boardService;
    @Autowired FileService fileService;

    @GetMapping()
    
//    public ResponseEntity<?> getAllBoard(BoardEntity boardVO
    		
            public ResponseEntity<?> getAllBoard(
//            		//,@RequestParam(value = "password2", required = true) int password2
            		@RequestParam(value = "size", required = false, defaultValue = "10") int size,
            		@RequestParam(value = "searchType", required = false) String searchType,
            		@RequestParam(value = "keyword", required = false) String keyword
            
    ) {
        try {
            // List<Boards> boardList = boardService.list(); 기존엔 전체 조회기능 테스트를 위해 List 타입으로 넘겼음

//        	 PageInfo<Boards> pageInfo = boardService.list(page, size);
            PageInfo<Boards> pageInfo = boardService.list(page, size, searchType, keyword);
//            PageInfo<Boards> pageInfo = boardService.list(boardVO.getPage(), boardVO.getSize(), boardVO.getSearchType(), boardVO.getKeyword());
//            PageInfo<Boards> pageInfo = boardService.list(boardVO);
            
            // 페이지 네이션 객체 생성
            Pagination pagination = new Pagination();
            // setter로 페이지, 사이즈, 페이지 정보의 총 갯수 셋팅
            pagination.setPage(boardVO.getPage());
            pagination.setSize(boardVO.getSize());
            pagination.setTotal(pageInfo.getTotal());
            
            // 응답 받기
            Map<String, Object> response = new HashMap<String, Object>();
            // 받은 응답 꺼내오기
            response.put("list", pageInfo.getList());
            response.put("pagination", pagination);

            // return new ResponseEntity<>(boardList, HttpStatus.OK); // 1. 우선 게시글 목록 전체를 리턴 하기
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
        	e.printStackTrace();
            log.error("BoardController: getAllBoard API 처리 중 오류 발생");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* 초기 작성 코드 getOneBoard (파일 업로드 적용 전 게시판에 사용하던 로직)
        @GetMapping("/{id}")
        // @PathVariable("id") 간혹 여기다 조회할 id 값을 적지 않으면 springboot3 버전에 따라 지원되지 않는 경우가 있다고 한다.
         public ResponseEntity<?> getOneBoard(@PathVariable("id") String id) {
            try {
                Boards board = boardService.selectById(id);
                return new ResponseEntity<>(board, HttpStatus.OK);
            } catch (Exception e) {
                log.error("BoardController: getOneBoard API 처리 중 오류 발생", id, e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    */

    @GetMapping("/{id}")
     public ResponseEntity<?> getOneBoard(
             @PathVariable("id") String id
    ) {
        try {
            Boards board = boardService.selectById(id);
            // 파일 목록 조회
            Files file = new Files();
            file.setPTable("boards");
            file.setPNo(board.getNo());
            List<Files> fileList = fileService.listByParent(file);
            Map<String, Object> response = new HashMap<>();
            response.put("board", board);
            response.put("fileList", fileList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
        	e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* 초기 작성 코드 createBoard (파일 관련 기능 적용 전 게시판에 사용하던 로직)
        @PostMapping()
        public ResponseEntity<?> createBoard(@RequestBody Boards board) {
            try {
                boolean result = boardService.insert(board);
                if( result ) {
                    return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    */

    /* @RequestBody 붙이고 안 붙이고 차이
      - @RequestBody / O : application/json, application/xml
      - @RequestBody / X : application/form-data, application/x-www-form-urlencoded
     */
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBoardFormData(Boards board) {
        log.info("게시글 및 파일 등록 - multipart/form-data");
        log.info("createBoardFormData -> board가 잘 넘어오는지 확인 : " + board);
        try {
            boolean result = boardService.insert(board);
            if( result ) {
                return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
        	e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createBoardJSON(HttpServletRequest req) {
        log.info("게시글 등록 - application/json");

        try {
            // 1. 요청 바디(JSON) 읽어오기
            StringBuilder jsonBody = new StringBuilder();
            String line;
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }
            log.info("받은 JSON 데이터: " + jsonBody.toString());

            // 2. JSON 문자열을 Boards 객체로 변환
            ObjectMapper mapper = new ObjectMapper();
            Boards board = mapper.readValue(jsonBody.toString(), Boards.class);
            log.info("매핑된 Boards 객체: " + board.toString());

            // 3. 데이터 저장
            boolean result = boardService.insert(board);
            if (result) {
                return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
            }

        } catch (IOException e) {
            log.error("JSON 변환 실패: ", e);
            return new ResponseEntity<>("JSON 파싱 오류", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("게시글 등록 중 오류 발생: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* 게시판 수정 로직 (기존)
	    @PutMapping()
	    public ResponseEntity<?> updateBoard(@RequestBody Boards board) {
	        try {
	            boolean result = boardService.updateById(board);
	            if( result ) {
	                return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
	            }
	        } catch (Exception e) {
	
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
    */
    
    // 게시판 파일 관련 기능 적용 후 수정 로직 (MediaType.MULTIPART_FORM_DATA_VALUE - Multipart_form_data 전용)
    @PutMapping(value= "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBoardForm(Boards board) {	// formdata 방식일때는 @RequestBody 제외
    	try {
    		boolean result = boardService.updateById(board);
    		if( result ) {
    			return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    		} else {
    			return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
    		}
    	} catch (Exception e) {
    		
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    // 게시판 파일 관련 기능 적용 후 수정 로직 (MediaType.APPLICATION_JSON_VALUE - JSON 전용)
    @PutMapping(value= "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBoard(@RequestBody Boards board) {
    	try {
    		boolean result = boardService.updateById(board);
    		if( result ) {
    			return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    		} else {
    			return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
    		}
    	} catch (Exception e) {
    		
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroyBoard(@PathVariable("id") String id) {
        try {
            boolean result = boardService.deleteById(id);
            if( result ) {
                return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
