package kr.or.ddit.board.dao;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.vo.AttatchVO;
import kr.or.ddit.vo.BoardVO;

/**
 * 
 * Attach 테이블을 대상으로 한 CRD
 *
 */
public interface AttatchDAO {

	public int insertAttatches(BoardVO board, SqlSession sqlSession); // 신규 첨부파일
	
	/**
	 * 다운로드시 사용할 하나의 첨부파일에 대한 메타데이터 조회
	 * @param attNo
	 * @return
	 */
	public AttatchVO selectAttatch(int attNo); // 기존 첨부파일에서 한 건의 메타데이터에서 사용
	
	public int deleteAttatches(BoardVO board, SqlSession sqlSession); // 첨부파일 삭제
	
}
