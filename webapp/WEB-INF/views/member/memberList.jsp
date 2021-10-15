<%@page import="java.util.List"%>
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
			<th>회원아이디</th>
			<th>이름</th>
			<th>휴대폰</th>
			<th>이메일</th>
			<th>주소(상위주소만)</th>
			<th>마일리지</th>
		</tr>
	</thead>
	<tbody id="listBody">
		
	</tbody>
	<tfoot>
		<tr>
			<td colspan="6">
				<div id="pagingArea">
					
				</div>
				<div id="searchUI">
					<select name="searchType">
						<option value>전체</option>
						<option value="name">이름</option>
						<option value="address">지역</option>
					</select>
					<input type="text" name="searchWord"  />
					<input type="button" value="검색" id="searchBtn"/>
				</div>
			</td>
		</tr>
	</tfoot>
</table>
<form id="searchForm">
	<input type="text" name="searchType" />
	<input type="text" name="searchWord" />
	<input type="text" name="page" />
</form>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/custom/paging.js"></script>
<script type="text/javascript">
	
	
	 let listBody = $("#listBody")/*.on("click", "tr", function(){
		let member = $(this).data("member");
		if(!member) return false;
		let memId = member.memId;
		location.href="${pageContext.request.contextPath }/member/memberView.do?who="+memId;
	}); */ 
	
	let pagingArea = $("#pagingArea");
	
	let searchForm = $("#searchForm").paging().ajaxForm({
		dataType:"json",
		success:function(resp){ 
 			console.log(resp); // resp 값 넘어옴, 페이징 관련 값들
 			
			listBody.empty();
			pagingArea.empty();
			
			searchForm.find("[name='page']").val(""); // searchForm 에서 이름이 page 인 val 값을 "" 으로 바꿔라
			
			let memberList = resp.dataList; // memberList 에 controller 에서 받아온 resp.dataList 값을 넣는다(회원 리스트)
			console.log(memberList); // resp 값 넘어옴, 페이징 관련 값들
			
			let pagingHTML = resp.pagingHTML; // pagingHTML 에 controller 에서 받아온 pagingHTML 값을 넣는다.
			console.log(pagingHTML); // resp 값 넘어옴, 페이징 관련 값들
			
			let trTags = [];
			if(memberList){
				$.each(memberList, function(idx, member){
					let trTag = $("<tr>").append(
						$("<td>").text(member.memId),	
						$("<td>").text(member.memName),	
						$("<td>").text(member.memHp),	
						$("<td>").text(member.memAdd1),	
						$("<td>").text(member.memMail),	
						$("<td>").text(member.memMileage)	
					).data("member", member)
					 .css("cursor", "pointer");
					trTags.push(trTag);
				});
			}else{
				let trTag = $("<tr>").html(
								$("<td>").attr({
									colspan:"6"
								}).text("조건에 맞는 회원이 없음.")		
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

















