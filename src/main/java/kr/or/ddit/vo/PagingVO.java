package kr.or.ddit.vo;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 전체 페이징 속성을 연산하기 위해, setTotalRecord 와 setCurrentPage 호출 필요.
 *
 */
@Getter
@NoArgsConstructor
public class PagingVO<T> {                                         
	
	public PagingVO(int screenSize, int blockSize) {
		super();
		this.screenSize = screenSize;
		this.blockSize = blockSize;
	}

	private int totalRecord;			// 총 게시물 수
	private int screenSize = 10;		// 한 페이지당 리스트 
	private int blockSize = 5;			// 한 화면에 보여줄 페이지 수
	private int currentPage;			// 현재 페이지 번호
	
	private int totalPage;				// 총 페이지 수
	private int endRow;					// 종료 게시물 번호
	private int startRow;				// 시작 게시물 번호
	private int endPage;				// 종료 페이지
	private int startPage;				// 시작 페이지
	
	private List<T> dataList;
	
	private T detailSearch;
	
	// 상세 검색용
	public void setDetailSearch(T detailSearch) {
		this.detailSearch = detailSearch;
	}
	
	// 단순 키워드 검색용 
	private SearchVO searchVO;
	
	public void setSearchVO(SearchVO searchVO) {
		this.searchVO = searchVO;
	}
	
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}
	
	public void setTotalRecord(int totalRecord) {
		
		// 테이블에서 총 카운트를 가져온다.
		this.totalRecord = totalRecord;
		
		
		totalPage = (totalRecord + (screenSize-1))/screenSize;
	}
	
	public void setCurrentPage(int currentPage) {
		
		// 현재 선택된 페이지, 없으면 1 로 세팅
		this.currentPage = currentPage;
		endRow = currentPage * screenSize;
		startRow = endRow - (screenSize - 1);

		endPage = blockSize * ((currentPage + (blockSize - 1))/blockSize);
		startPage = endPage - (blockSize - 1);
	}
	
//	<a href="?page=1">1</a>
	private static final String PAINGPATTERN = "<a href='#' data-page='%d'>%s</a>";
	private static final String CURRENTPATTERN = "<a href='#'>[%s]</a>";
	public String getPagingHTML() {
		StringBuffer html = new StringBuffer();
		endPage = endPage > totalPage ? totalPage : endPage;
		if(startPage > blockSize) {
			html.append(String.format(PAINGPATTERN, (startPage - blockSize), "이전"));
		}
		for(int page = startPage; page <= endPage; page++) {
			if(page==currentPage) {
				html.append(String.format(CURRENTPATTERN, page));
			}else {
				html.append(String.format(PAINGPATTERN, page, page));
			}
		}
		if(endPage < totalPage) {
			html.append(String.format(PAINGPATTERN, (endPage + 1), "다음"));
		}
		return html.toString();
	}
}



