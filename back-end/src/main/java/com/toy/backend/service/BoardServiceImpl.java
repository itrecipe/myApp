package com.toy.backend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.toy.backend.domain.Boards;
import com.toy.backend.domain.Files;
import com.toy.backend.mapper.BoardMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BoardServiceImpl implements BoardService {

    @Autowired BoardMapper boardMapper; // 메퍼 의존성 주입

    @Autowired FileService fileService;

    @Override
    public List<Boards> list() {
        return boardMapper.list();
    }

    @Override
    public Boards select(Long no) {
        return boardMapper.select(no);
    }

    @Override
    public Boards selectById(String id) {
        return boardMapper.selectById(id);
    }

    /*    [기존 게시판 insert 서비스 로직]
          @Override
          public boolean insert(Boards entity) {

              return boardMapper.insert(entity) > 0;
              insert부터 deleteById까지 1건 이라도 데이터가
              있으면 true 또는 false로 응답하도록 느슨한 연결을 해준다.
          }
    */

    @Override
    public boolean insert(Boards entity) {
        // 게시글 등록
        int result = boardMapper.insert(entity); // 등록 먼저 해주기

        // 파일 업로드
        result += upload(entity);
        return result > 0;
    }

    /*
        - 파일 업로드
        @param entity
        @return
     */
    public int upload(Boards entity) {
        int result = 0;
        String pTable = "boards";
        Long pNo = entity.getNo();

        List<Files> uploadFileList = new ArrayList<>();

        MultipartFile mainFile = entity.getMainFile(); // 메인 파일은 별도로 꺼내서 넣어주기
        if( mainFile != null && !mainFile.isEmpty() ) {
            Files mainFileInfo = new Files();
            mainFileInfo.setPTable(pTable);
            mainFileInfo.setPNo(pNo);
            mainFileInfo.setData(mainFile);
            mainFileInfo.setType("MAIN");
        }

        // 파일 테이블의 데이터를 정리해서 넘겨주기
        List<MultipartFile> files = entity.getFiles();
        if( files != null && !files.isEmpty() ) {
//        String pTable = "boards";
//        Long pNo = entity.getNo();      // 설정?
        List<MultipartFile> files = entity.getFiles();
        if ( files != null && !files.isEmpty() ) {
            for(MultipartFile multipartFile : files) {
                if( multipartFile.isEmpty() )
                    continue;
                Files fileInfo = new Files();
                fileInfo.setPTable(pTable);
                fileInfo.setPNo(pNo);
                fileInfo.setData(multipartFile);
                fileInfo.setType("SUB");
                uploadFileList.upload(fileInfo);
            }
        }

        return result;
    }

    @Override
    public boolean update(Boards entity) {
        return boardMapper.update(entity) > 0;
    }

    @Override
    public boolean updateById(Boards entity) {
        return boardMapper.updateById(entity) > 0;
    }

    /*
        - 개별 파일 삭제

        - 부모 기준 여러 파일 삭제
            - 게시글 삭제 시,
            첨부파일이 종속 삭제

         boards <- files
         p_table : boards
         p_no : (board PK)

        - 선택 파일 삭제
        client ---> server // 클라이언트에서 서버측으로 id 문자열을 전달한다.
        [id1, id2, id3]

     */

    @Override
    public boolean delete(Long no) {
        // 게시글 삭제
        boolean result = boardMapper.delete(no) > 0;

        // 종속된 첨부파일 삭제
        Files file = new Files();
        file.setPTable("boards");
        file.setPNo(no);
        int deletedCount = fileService.deleteByParent(file);
        log.info(deletedCount + "개의 파일이 삭제 되었습니다.");
        return result;
    }

    @Override
    public boolean deleteById(String id) {
        // 게시글 조회 (삭제를 진행하려면 게시글 id와 no을 알아야 한다.)
        Boards board = boardMapper.selectById(id);
        Long no = board.getNo();

        // 게시글 삭제
        boolean result = boardMapper.deleteById(id) > 0;

        // 종속된 첨부파일 삭제
        Files file = new Files();
        file.setPTable("boards");
        file.setPNo(no);
        int deletedCount = fileService.deleteByParent(file);
        log.info(deletedCount + "개의 파일이 삭제 되었습니다.");
        return result;
    }

    // 페이징 (페이지 리스트 출력)
    @Override
    public PageInfo<Boards> list(int page, int size) {
        PageHelper.startPage(page,size);
        List<Boards> list = boardMapper.list();
        PageInfo<Boards> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
}
