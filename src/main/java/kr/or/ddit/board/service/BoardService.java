package kr.or.ddit.board.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.vo.AttatchVO;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.PagingVO;

public interface BoardService {
	
	public ServiceResult createBoard(BoardVO board);
		
	public List<BoardVO> retrieveBoardList(PagingVO<BoardVO> pagingVO);

	public BoardVO retrieveBoard(int boNo);
	
	public ServiceResult modifyBoard(BoardVO board);
	
	public ServiceResult removeBoard(BoardVO board);
	
	public AttatchVO download(int attNo); // 한 건의 첨부파일 다운로드 메소드, 첨부파일이 없을시 예외처리

	int processAttatches(BoardVO board, SqlSession sqlSession);

}
