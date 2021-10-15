package kr.or.ddit.board.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.PagingVO;

/**
 * 
 * FreeBoard 테이블을 대상으로 한 CRUD
 *
 */
public interface BoardDAO {
	
	public int insertBoard(BoardVO board, SqlSession sqlSession); 
	public int selectTotalRecord(PagingVO<BoardVO> pagingVO); // 페이징 처리를 위한 DAO, 전체 레코드 수 출력
	public List<BoardVO> selectBoardList(PagingVO<BoardVO> pagingVO); // 페이징 처리를 위한 DAO, 검색 조건
	public BoardVO selectBoard(int boNo);
	public int uploadBoard(BoardVO board);
	public int deleteBoard(int boNo, SqlSession sqlSession);
	
	public int incrementCount(Map<String, Object> pMap); // 조회수,추천수,신고수 증가 메소드
	
}
