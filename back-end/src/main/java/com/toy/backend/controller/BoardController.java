package com.toy.backend.controller;

import com.github.pagehelper.PageInfo;
import com.toy.backend.domain.Boards;
import com.toy.backend.domain.Pagination;
import com.toy.backend.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@CrossOrigin("*") // *은 전체 경로 지정을 의미
@RestController
@RequestMapping("/boards")
public class BoardController {

    @Autowired BoardService boardService;

    @GetMapping()
    public ResponseEntity<?> getAllBoard(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        try {
            // List<Boards> boardList = boardService.list(); 기존엔 전체 조회기능 테스트를 위해 List 타입으로 넘겼음

            PageInfo<Boards> pageInfo = boardService.list(page, size);
            // 페이지 네이션 객체 생성
            Pagination pagination = new Pagination();
            // setter로 페이지, 사이즈, 페이지 정보의 총 갯수 셋팅
            pagination.setPage(page);
            pagination.setSize(size);
            pagination.setTotal(pageInfo.getTotal());
            // 응답 받기
            Map<String, Object> response = new HashMap<String, Object>();
            // 받은 응답 꺼내오기
            response.put("list", pageInfo.getList());
            response.put("pagination", pagination);

            // return new ResponseEntity<>(boardList, HttpStatus.OK); // 1. 우선 게시글 목록 전체를 리턴 하기
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("BoardController: getAllBoard API 처리 중 오류 발생");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{id}")
    // @PathVariable("id") 간혹 여기다 조회할 id값을 적지 않으면 springboot3 버전에 따라 지원되지 않는 경우가 있다고 한다.

     public ResponseEntity<?> getOneBoard(@PathVariable("id") String id) {
        try {
            Boards board = boardService.selectById(id);
            return new ResponseEntity<>(board, HttpStatus.OK);
        } catch (Exception e) {
            log.error("BoardController: getOneBoard API 처리 중 오류 발생", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
