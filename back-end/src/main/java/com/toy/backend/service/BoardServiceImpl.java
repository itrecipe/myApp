package com.toy.backend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.toy.backend.domain.Boards;
import com.toy.backend.domain.Files;
import com.toy.backend.mapper.BoardMapper;
import com.toy.backend.vo.BoardEntity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    /* 초기 작성 코드 (insert)
       @Override
       public boolean insert(Boards entity) {

          return boardMapper.insert(entity) > 0;
          insert부터 deleteById까지 1건 이라도 데이터가
          있으면 true 또는 false로 응답하도록 느슨한 연결을 해준다.
       }
    */

    /* 게시판 + 파일 관련 기능 등록 처리 (기존)
	    @Override
	    @Transactional // 게시글 등록 처리가 되지 않으면 파일 업로드 처리도 되지 않도록 @Transactional을 걸어 준다.
	    public boolean insert(Boards entity) {
	    	// 1. 게시글 먼저 등록하기
	        int result = boardMapper.insert(entity);
	        
	        // 2. 파일 업로드 작업
	        String pTable = "boards";
	        Long pNo = entity.getNo();
	           no는 autoincrement로 자동증가 하기 때문에 mybatis로 autoincrement
	           된 값을 다시 insert(entity) 객체에 넣어주는 옵션이 필요하다.
	         
	
	        List<Files> uploadFileList = new ArrayList<>();
	
	        // 메인 파일들을 한번에 담아 가져오기
	        MultipartFile mainFile = entity.getMainFile();
	        if( mainFile != null && !mainFile.isEmpty() ) {
	            Files mainFileInfo = new Files();
	            mainFileInfo.setPTable(pTable);
	            mainFileInfo.setPNo(pNo);
	            mainFileInfo.setData(mainFile);
	            mainFileInfo.setType("MAIN");
	            uploadFileList.add(mainFileInfo);
	        }
	
	        List<MultipartFile> files = entity.getFiles();
	        if( files != null && !files.isEmpty() ) {
	            for (MultipartFile multipartFile : files) {
	                if( multipartFile.isEmpty() )
	                    continue;
	                Files fileInfo = new Files();
	                fileInfo.setPTable(pTable);
	                fileInfo.setPNo(pNo);
	                fileInfo.setData(multipartFile);
	                fileInfo.setType("SUB");
	                uploadFileList.add(fileInfo);
	            }
	        }
	        try {
	            result += fileService.upload(uploadFileList);
	        } catch (Exception e) {
	            log.error("게시글 파일 업로드 중 에러 발생!");
	            e.printStackTrace();
	        }
	        return result > 0;
	    }
    */
	
    
    // 게시판 + 파일 관련 기능 확장 후 등록 처리
    @Override
    @Transactional // 게시글 등록 처리가 되지 않으면 파일 업로드 처리도 되지 않도록 @Transactional을 걸어 준다.
    public boolean insert(Boards entity) {
    	// 1. 게시글 먼저 등록하기
    	int result = boardMapper.insert(entity);
    	
    	// 2. 파일 업로드 작업
    	result += upload(entity);
    
    	return result > 0;
    }
    
    /* 파일 업로드
      @param entity
      @return
     */
    public int upload(Boards entity) {
    	int result = 0;
    	
    	String pTable = "boards";
    	Long pNo = entity.getNo();
    	/* no는 autoincrement로 자동증가 하기 때문에 mybatis로 autoincrement
           된 값을 다시 insert(entity) 객체에 넣어주는 옵션이 필요하다.
    	 */
    	
    	List<Files> uploadFileList = new ArrayList<>();
    	
    	// 메인 파일들을 한번에 담아 가져오기
    	MultipartFile mainFile = entity.getMainFile();
    	if( mainFile != null && !mainFile.isEmpty() ) {
    		Files mainFileInfo = new Files();
    		mainFileInfo.setPTable(pTable);
    		mainFileInfo.setPNo(pNo);
    		mainFileInfo.setData(mainFile);
    		mainFileInfo.setType("MAIN");
    		uploadFileList.add(mainFileInfo);
    	}
    	
    	List<MultipartFile> files = entity.getFiles();
    	if( files != null && !files.isEmpty() ) {
    		for (MultipartFile multipartFile : files) {
    			if( multipartFile.isEmpty() )
    				continue;
    			Files fileInfo = new Files();
    			fileInfo.setPTable(pTable);
    			fileInfo.setPNo(pNo);
    			fileInfo.setData(multipartFile);
    			fileInfo.setType("SUB");
    			uploadFileList.add(fileInfo);
    		}
    	}
    	try {
    		result += fileService.upload(uploadFileList);
    	} catch (Exception e) {
    		log.error("게시글 파일 업로드 중 에러 발생!");
    		e.printStackTrace();
    	}
    	return result;
    }

    /*	게시판 기본 수정 (기존)
	    @Override
	    public boolean update(Boards entity) {
	        return boardMapper.update(entity) > 0;
	    }
    */
    
    // 게시판 + 파일 관련 기능 확장 후 수정
    @Override
    public boolean update(Boards entity) {
    	// 게시글 수정
    	int result = boardMapper.update(entity);
    	
    	// 파일 업로드 작업
    	result += upload(entity);
    	
    	return result > 0;
    }

    /* 게시판 기본 수정 (updateById - 기존)
	    @Override
	    public boolean updateById(Boards entity) {
	        return boardMapper.updateById(entity) > 0;
	    }
	*/
    
    // 게시판 + 파일 관련 기능 확장 후 수정 (updateById - 개선)
    @Override
    public boolean updateById(Boards entity) {
    	
    	// 게시글 수정
    	int result = boardMapper.updateById(entity);
    	
    	/* 이전 게시글의 id를 확인하여 정보를 조회 
    	   파일 업로드 작업
    	*/
    	Boards oldBoard = boardMapper.selectById(entity.getId());
    	entity.setNo( oldBoard.getNo() );
    	result += upload(entity);
    	
    	return result > 0;
    }

//    @Override
//    public int upload(List<Files> fileList) throws Exception {
//        int result = 0;
//        if( fileList == null || fileList.isEmpty() )
//            return result;
//
//        for (Files files : fileList) {
//            result += upload(files);
//        }
//        return result;
//    }

    /*
        - 파일 업로드
        @param entity
        @return
     */

    /* public int upload(Boards entity) {
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
    }*/

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
        log.info(deletedCount + "건의 파일이 삭제 되었습니다.");
        return result;
    }

    // 페이징 (페이지 리스트 출력) - 기존 게시판 코드
    /*
	    @Override
	    public PageInfo<Boards> list(int page, int size) {
	    	List<Boards> list = boardMapper.list();
	        PageHelper.startPage(page, size);
	        PageInfo<Boards> pageInfo = new PageInfo<>(list);
	        return pageInfo;
	    }
    */
    
    // 검색 처리
    @Override
    public PageInfo<Boards> list(int page, int size, String searchType, String keyword) {
    	
        
    	// 페이지 네이션 적용 
    	PageHelper.startPage(page, size);
    	
    	// 검색 조건에 맞는 게시글 조회
    	List<Boards> boardList = boardMapper.search(searchType, keyword);
    	
    	// 조회 결과를 PageInfo로 변환하여 반환
    	
        return new PageInfo<>(boardList);
    }

    /*
		@Override
		public PageInfo<Boards> list(BoardEntity boardVO) {
	
	    	// 페이지 네이션 적용 
	    	PageHelper.startPage(boardVO.getPage(), boardVO.getSize());
	    	
	    	// 검색 조건에 맞는 게시글 조회
	    	List<Boards> boardList = boardMapper.search(searchType, keyword);
	    	
	    	// 조회 결과를 PageInfo로 변환하여 반환
	    	
	        return new PageInfo<>(boardList);
	
		}
	*/
}
