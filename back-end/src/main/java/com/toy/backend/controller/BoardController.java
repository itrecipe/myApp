package com.toy.backend.controller;

import com.toy.backend.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin("*") // *은 전체 경로 지정을 의미
@RestController
@RequestMapping("/boards")
public class BoardController {

    @Autowired BoardService boardService;

    @GetMapping()
    public RespinseEntity<?> getAllBoard() {
        
    }
}
