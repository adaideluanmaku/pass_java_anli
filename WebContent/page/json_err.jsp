<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="/pass_java_anli/css/nameliebiao.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>json_err查看</title>
</head>
<body>
<div class="bg">
<fieldset>
	<legend><c:out value="${anliname }"></c:out> </legend>
	<c:if test="${empty json_err }">
		<c:out value="对比结果相同"></c:out>
	</c:if>
	<c:forEach  var="list2" items="${json_err}" varStatus="status">
	
	<c:if test="${status.count%2==1}">
	<font color="red"><c:out value="${list2}"></c:out></font><br>
	</c:if>
	<c:if test="${status.count%2==0}">
	<font color="blue"><c:out value="${list2}"></c:out></font><br>
	</c:if>
	<c:if test="${status.count%2==0}">
	<p>--------------------------------------------------</p>
	</c:if>
	</c:forEach>
</fieldset>
</div>
</body>
</html>