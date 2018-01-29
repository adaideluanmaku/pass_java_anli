<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="/pass_java_anli/js/jquery.js"></script>

<link rel="stylesheet" type="text/css" href="/pass_java_anli/css/login.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>坚果-PASS整合测试用例</title>
</head>
<body>
<div class="bg">
	<div class="logo">
		<font id="text" size="10" color="red">坚果-苦荞茶</font>
	</div>
	<div class="index">
		<form action="Login.action" method="post">
			程序版本号:<select name="version">
				<option value="1609">1609</option>
				<option value="1612">1612</option>
			</select>
			<input type="submit" value="登录">
		</form>
	</div>
</div>
</body>
</html>