<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="/pass_java_anli/js/jquery.js"></script>
<script type="text/javascript">
function get_shuju(){
	var anliname=$("#anliname1").val();
	var tablename=$("#tablename1").val();
	var selectsql=$("#selectsql1").val();
	var updatesql=$("#updatesql1").val();
	var recsql=$("#recsql1").val();
	
	$("#anliname").attr("value",anliname);
	$("#tablename").attr("value",tablename);
	$("#selectsql").attr("value",selectsql);
	$("#updatesql").attr("value",updatesql);
	$("#recsql").attr("value",recsql);
	return true;
}
</script>
<link rel="stylesheet" type="text/css" href="/pass_java_anli/css/login.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>坚果-整合测试用例</title>
</head>
<body>
<div class="bg">
<div>
	<form action="Redis_keys.action" method="post">
		案例名称：<input type="text" name="anliname" value="${anliname }">
		表名：<input type="text" name="tablename" value="${tablename }">
		<input type="hidden" name="version" value="${param.version }">
		<input type="hidden" name="res" value="2">
		<input type="submit" value="查询">
	</form>
	
	<p>-----------------------------------------------------------------------------------</p>
	<table>
		<tr>
			<td>
				案例名称：
			</td>
			<td>
				<input id="anliname1" type="text" name="anliname" value="${anliname1 }">
			</td>
		</tr>
		<tr>
			<td>
				表名：
			</td>
			<td>
				<input id="tablename1" type="text" name="tablename" value="${tablename1 }">
			</td>
		</tr>
		<tr>
			<td>
				查询sql：
			</td>
			<td>
				<textarea id="selectsql1" name="selectsql" style="height:50px;width:700px" >${selectsql1 }</textarea>
			</td>
		</tr>			
		<tr>
			<td>
				更新sql：
			</td>
			<td>
				<textarea id="updatesql1" name="updatesql" style="height:50px;width:700px" >${updatesql1 }</textarea>
			</td>
		</tr>
		<tr>
			<td>
				还原sql：
			</td>
			<td>
				<textarea id="recsql1" name="recsql" style="height:50px;width:700px" >${recsql1 }</textarea>
			</td>
		</tr>				
		<tr>
			<td>
				<form  name="from1" action="Redis_keys.action" method="post" onsubmit="return get_shuju()">
				<input type="hidden" name="res" value="1">
				<input type="hidden" name="version" value="${param.version }">
				<input id="anliname" type="hidden" name="anliname" value="${anliname1 }">
				<input id="tablename" type="hidden" name="tablename" value="${tablename1 }">
				<input id="selectsql" type="hidden" name="selectsql" value="${selectsql1 }">
				<input id="updatesql" type="hidden" name="updatesql" value="${updatesql1 }">
				<input id="recsql" type="hidden" name="recsql" value="${recsql1 }">
				<input type="submit" value="保存">
				</form>
			</td>
			<td>
				<form name="from2" action="Redis_keys.action" method="post" onsubmit="return get_shuju()">
					<input type="hidden" name="res" value="3">
					<input type="hidden" name="version" value="${param.version }">
					<input id="anliname" type="hidden" name="anliname" value="${anliname1 }">
					<input id="tablename" type="hidden" name="tablename" value="${tablename1 }">
					<input type="submit" value="删除">
				</form>
			</td>
		</tr>		
	
		
	</table>
	<c:if test="${!empty message}">
		<c:out value="${message }"></c:out>
	</c:if>
</div>
</div>
</body>
</html>