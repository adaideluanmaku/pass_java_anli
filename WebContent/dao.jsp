<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="/pass_java_anli/css/login.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>坚果-系统功能</title>
</head>
<body>
	<table>
		<tr>
			<td style="width:800px"><font size="3" color="#ff8040" >版本号${param.version }</font></td>
		</tr>
		<tr>
			<td >
				<form action="Login.action" method="post">
					<input type="hidden" name ="req" value="1">
					<input type="hidden" name ="version" value="${param.version }">
					<input type="submit" value="导入win案例">
					<select name="dao" >
						<option value="100">不导</option>
						<option value="112">模糊导</option>
						<option value="111">全导</option>
						<option value="1">自动审查</option>
						<option value="2">手动审查</option>
						<option value="3">说明书</option>
						<option value="4">浮动窗口</option>
						<option value="">详细信息</option>
						<option value="">用药理由</option>
						<option value="">右键菜单</option>
						<option value="">模块菜单</option>
					</select>
					<input type="text" name="anliname" value="">只有选择“模糊导”时填写案例名称有用(匹配like '%name%')
				</form>
			</td>
		</tr>
		<tr>
			<td style="width:800px">
				<form action="Login.action" method="post">
					<input type="hidden" name ="req" value="3">
					<input type="hidden" name ="version" value="${param.version }">
					<input type="submit" value="备份案例库">
				</form>
			</td>
		</tr>
		<tr>
			<td >
				<form action="Login.action" method="post">
					<input type="hidden" name ="req" value="4">
					<input type="hidden" name ="version" value="${param.version }">
					<input type="submit" value="还原案例库">
				</form>
			</td>
		</tr>
		<tr>
			<td>
				<form action="Login.action" method="post">
					<input type="hidden" name ="req" value="2">
					<input type="hidden" name ="version" value="${param.version }">
					<input type="submit" value="更新所有数据HISCODE">
					<input type="text" name=hiscode value="">输入HISCODE
				</form>
			</td>
		</tr>
		<tr>
			<td><a href="Passredisquery.action?req=999" target="_blank"  />redis查询</td>
		</tr>
		<tr>
			<td><a href="Redis_keys.action?version=${param.version}" target="_blank"  >shoudong_redis表维护</a></td>
		</tr>
		<tr>
			<td><a href="ServerIP.action" target="_blank"  >访问地址维护</a></td>
		</tr>
	</table>
	<c:out value="${message }"></c:out>
</body>
</html>