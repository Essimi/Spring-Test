package kr.or.ddit.board.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.or.ddit.board.dao.AttatchDAO;
import kr.or.ddit.board.dao.AttatchDAOImpl;
import kr.or.ddit.board.dao.BoardDAO;
import kr.or.ddit.board.dao.BoardDAOImpl;
import kr.or.ddit.common.servlet.PKNotFoundException;
import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.mybatis.CustomSqlSessionFactoryBuilder;
import kr.or.ddit.vo.AttatchVO;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.PagingVO;

public class BoardServiceImpl implements BoardService {
	
	private SqlSessionFactory sqlSessionFactory = CustomSqlSessionFactoryBuilder.getSqlSessionFactory();
	
	private BoardDAO boardDAO = new BoardDAOImpl();
	private AttatchDAO attatchDAO = new AttatchDAOImpl();
	
	private String saveFolderPath = "/Users/essimi/Desktop/saveFiles";
	private File saveFolder = new File(saveFolderPath);
	
	@Override
	public int processAttatches(BoardVO board, SqlSession sqlSession) {
		
		int rowcnt = 0;
		
		List<AttatchVO> attatchList = board.getAttatchList(); // 파일데이터를 추출해냄
		
		if(attatchList != null && !attatchList.isEmpty()) { // 데이터 검증
			
			rowcnt = attatchDAO.insertAttatches(board, sqlSession); // 데이터 저장
			
			//if(1 == 1) throw new RuntimeException("강제 발생 예외");
			
			try {
				for(AttatchVO atch : attatchList) { // 데이터를 추출 후 저장, List 이기 때문에 반복문 진입
					atch.saveTO(saveFolder);
				}
			}catch(IOException e) {
				throw new RuntimeException(e);
			}
		}
		return rowcnt;
	}
	
	
	
	@Override
	public ServiceResult createBoard(BoardVO board) {
		
		try(
			SqlSession sqlSession = sqlSessionFactory.openSession();	// transaction 시작
		){
			ServiceResult result = null;
			
			int rowcnt = boardDAO.insertBoard(board, sqlSession);
			
			if(rowcnt > 0) {
				rowcnt += processAttatches(board, sqlSession);
				result = ServiceResult.OK;
				sqlSession.commit();
			}else {
				result = ServiceResult.FAILED;
			}
			
			return result;
		
		}
	}

	@Override
	public List<BoardVO> retrieveBoardList(PagingVO<BoardVO> pagingVO) {
		
		// 게시글 데이터 조회
		List<BoardVO> boardList = boardDAO.selectBoardList(pagingVO);
		
		// 총 게시글수 조회
		pagingVO.setTotalRecord(boardDAO.selectTotalRecord(pagingVO));
		
		// 게시글 데이터 대입
		pagingVO.setDataList(boardList);
		
		// 게시글 데이터 리턴
		return boardList;
	}

	@Override
	public BoardVO retrieveBoard(int boNo) {
		
		BoardVO board = boardDAO.selectBoard(boNo);
		
		if(board == null) { // 요청한 글 번호가 없을때
			throw new PKNotFoundException(boNo + " 번 글은 없습니다.");
		}
		
		// 조회수 관련 코드. 게시글에 진입한 경우 현재 메소드 안에 있기 때문에, 
		// 게시글 진입 메소드(상단) + 조회수 증가 메소드(하단) 동시 실행
		Map<String, Object> pMap = new HashMap();
		pMap.put("boNo", boNo);
		pMap.put("incType", "BO_HIT"); // replace text 활용
		
		boardDAO.incrementCount(pMap);
		
		return board;
	}

	@Override
	public ServiceResult modifyBoard(BoardVO board) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private boolean authenticated(String inputPass, String savedPass){
		return savedPass.equals(inputPass);
	}

	@Override
	public ServiceResult removeBoard(BoardVO board) {
		
		BoardVO saved = retrieveBoard(board.getBoNo());
		
		ServiceResult result = null;
		
		if(authenticated(board.getBoPass(), saved.getBoPass())) {
			try(
				SqlSession sqlSession = sqlSessionFactory.openSession(); // 트랜잭션 오픈
			){
				List<AttatchVO> attatchList = saved.getAttatchList();
				attatchDAO.deleteAttatches(board,sqlSession);
				boardDAO.deleteBoard(board.getBoNo(), sqlSession);	
				
				for(AttatchVO tmp : attatchList) {
					FileUtils.deleteQuietly(new File(saveFolder, tmp.getAttSavename()));
				}
				
				result = ServiceResult.OK;
				sqlSession.commit();
			} // try end
			
		}else{
			result = ServiceResult.INVALIDPASSWORD;
		}
		
		return result;
	}

	@Override
	public AttatchVO download(int attNo) {
		// TODO Auto-generated method stub
		return null;
	}

}
