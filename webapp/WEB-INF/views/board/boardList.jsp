<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<jsp:include page="/includee/preScript.jsp" />
</head>
<body>
<table>
	<thead>
		<tr>
			<th>일련번호</th>
			<th>제목</th>
			<th>작성자</th>
			<th>조회수</th>
			<th>추천수</th>
			<th>작성일</th>
		</tr>
	</thead>	
	<tbody id = "listBody">
		<!-- 데이터 출력 부분 -->
	</tbody>
	<tfoot> <!-- 검색 조건 부분, 페이징 부분 -->
		<tr>
			<td colspan = "6">
				<div id = "pagingArea"> 
					<!-- 페이징 처리 부분 설정 -->
				</div>
				
				<div id = "searchUI"> <!-- 검색 조건 부분 -->
					<select name = "searchType">
						<option value>전체</option>
						<option value = "title">제목</option>
						<option value = "writer">작성자</option>
						<option value = "content">내용</option>
					</select>
					<input type = "text" name = "searchWord"/>
					<input type = "button" value = "검색" id = "searchBtn"/>
				</div>
			</td>
		</tr>
	</tfoot>
</table>

<form id = "searchForm"> <!-- 데이터 전송 처리 부분 -->
	<input type = "hidden" name = "searchType"/>
	<input type = "hidden" name = "searchWord"/>
	<input type = "hidden" name = "page"/>
</form>

<script type="text/javascript" src = "${pageContext.request.contextPath }/resources/js/custom/paging.js"></script>
<script type="text/javascript">

	let listBody = $("#listBody").on("click", "tr", function(){// listBody 지정
		let board = $(this).data("board");
		if(!board) return false;
		let boNo = board.boNo;
		location.href="${pageContext.request.contextPath }/board/boardView.do?who="+boNo;
	});  

	let pagingArea = $("#pagingArea"); // pagingArea 지정

	let searchForm = $("#searchForm").paging().ajaxForm({ // ajax 시작
	dataType:"json",
	success:function(resp){ 
		console.log(resp); // controller 의 resp 에서 값 넘어옴, 페이징 관련 값들
						   // 현재 resp 의 값에는 pagingVO 의 모든 값들이 담겨있다.
		
		listBody.empty(); // 새로 데이터 출력을 위해 비워줌
		pagingArea.empty(); // 새로 데이터 출력을 위해 비워줌
		
		searchForm.find("[name='page']").val(""); // searchForm 에서 이름이 page 인 val 값을 "" 으로 바꾼다
		
		let boardList = resp.dataList; // memberList 에 controller 에서 받아온 resp.dataList 값을 넣는다(회원 리스트)
		console.log(boardList); // resp 값 넘어옴, PagingVO.dataList 값 반환
		
		let pagingHTML = resp.pagingHTML; // pagingHTML 에 controller 에서 받아온 pagingHTML 값을 넣는다.
		//console.log(pagingHTML); // resp 값 넘어옴, PagingVO.pagingHTML 값 반환
		
		let trTags = []; // 태그를 출력하기 위한 변수 설정, 넣어줘야하는 태그값이 여러개일 수 있기 때문에 배열로 지정
		
		if(boardList){
			$.each(boardList, function(idx, board){ // boardList 의 이름을 board 로 지정 후 반복문 돌림
				let trTag = $("<tr>").append( // trTag 에 값 지정
					$("<td>").text(board.rnum),	
					$("<td>").text(board.boTitle)/* [${board.repCnt}] */,
					// 이미지 몇개인지 출력하는 부분
					/* <c:forEach begin="1" end="${board.atchCnt }">
						<img src="${pageContext.request.contextPath }/resources/images/attatchment.png" style="width: 10px; height: 10px;"/>
					</c:forEach> */	
					$("<td>").text(board.boWriter),	
					$("<td>").text(board.boHit),	
					$("<td>").text(board.boRec),
					$("<td>").text(board.boDate)
				).data("board", board)
				 .css("cursor", "pointer");
				trTags.push(trTag);
			});
		}else{
			let trTag = $("<tr>").html(
							$("<td>").attr({
								colspan:"6"
							}).text("조건에 맞는 게시글 없음.")		
						);
			trTags.push(trTag);
			
		}
		
		listBody.append(trTags);
		pagingArea.html(pagingHTML);
	},
	error:function(xhr, jQuery, error){
		console.log(jQuery);
		console.log(error);
	}
}).submit();


</script>
<jsp:include page="/includee/postScript.jsp" />
</body>
</html>
