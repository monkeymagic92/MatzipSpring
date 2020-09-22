<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>지도다</title>
</head>
<body>
	<!-- 주소 제대로 입력안하거나, 게스트일경우 바로 /rest/map 서블릿(@Controller) 로 보내서 view띄우겟다 -->
	<jsp:forward page="/rest/map"></jsp:forward>
</body>
</html>