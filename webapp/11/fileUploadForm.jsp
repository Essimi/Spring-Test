<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<!-- // BODY 를 만들기 위해 POST 설정, GET 방식 사용 불가 -->
<!-- enctype = multupart -> BODY 영역을 여러개로 쪼갬. 한번에 넘겨야하는 데이터가 여러개이기 때문. (문자열일수도, 이진데이터 형식일수도 있기 때문)
데이터가 섞이는걸 방지함.  -->
<!-- 폼태그 하나하나에 입력태그를 집어넣음. (form-data) -->
<form action="${pageContext.request.contextPath }/fileUploadSpec3.do" method="post" enctype="multipart/form-data">
	<input type="text" name="uploader" placeholder="업로더 이름"/>
	<input type="file" name="uploadFile" accept="image/*"/>
	<input type="file" name="uploadFile" accept="image/*"/>
	<input type="submit" value="업로드"  />
</form>
<div>
	<c:if test="${not empty  uploader}">
		${uploader }
		<c:remove var="uploader" scope="session"/>
	</c:if>
	<c:if test="${not empty  uploadFile_0}">
		<img src="${pageContext.request.contextPath }${uploadFile_0 }" />
		<c:remove var="uploadFile_0" scope="session"/>
	</c:if>
	
	<c:if test="${not empty  uploadFile_1}">
		<img src="${pageContext.request.contextPath }${uploadFile_1 }" />
		<c:remove var="uploadFile_1" scope="session"/>
	</c:if>
</div>
</body>
</html>