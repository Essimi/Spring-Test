package kr.or.ddit.board.dao;

import java.util.List;

import kr.or.ddit.vo.PagingVO;
import kr.or.ddit.vo.ReplyVO;

/**
 * FreeReply 테이블을 대상으로 한 CRUD
 *
 */
public interface ReplyDAO {
	public int insertReply(ReplyVO reply);
	public int selectTotalRecord(PagingVO<ReplyVO> pagingVO); // 페이징 처리를 위한 DAO, 전체 레코드 수 출력
	public List<ReplyVO> selectReplyList(PagingVO<ReplyVO> pagingVO); // 페이징 처리를 위한 DAO, 검색 조건
	public int updateReply(ReplyVO reply);
	public int deleteReply(int repNo);
	
}
