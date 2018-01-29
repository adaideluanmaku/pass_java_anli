<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="/pass_java_anli/easyui/themes/default/easyui.css">   
<link rel="stylesheet" type="text/css" href="/pass_java_anli/easyui/themes/icon.css">   
<script type="text/javascript" src="/pass_java_anli/easyui/jquery.min.js"></script>   
<script type="text/javascript" src="/pass_java_anli/easyui/jquery.easyui.min.js"></script>  

<script type="text/javascript">
function getnum(){
	var redisnum=$("#redisnum").val();
	if(redisnum=="" || redisnum==null){
		$("#redisnum").attr("value",999);
	}
	return true;
}
</script>
<link rel="stylesheet" type="text/css" href="/pass_java_anli/css/login.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>坚果-整合测试用例</title>
</head>
<body>
<div class="bg">
<p>事件触发</p>
<div id="p" class="easyui-panel" style="width:500px;height:200px;padding:10px;"   
        title="My Panel" data-options="iconCls:'icon-save',collapsible:true">   
    The panel content    
</div>  

<input class="easyui-combobox" name="language"   
        data-options="    
            url:'combobox_data.json',    
            valueField:'id',    
            textField:'text',    
            panelHeight:'auto',    
            onSelect:function(record){    
                alert(record.text)    
            }" />  

<br>
<p>拖动</p>
<div id="dd" class="easyui-draggable" data-options="handle:'#title',revert:'true',cursor:'pointer',delay:'3'" style="width:100px;height:100px;">    
<div id="title" style="background:#ccc;">title</div>
</div>

<p>放置</p>
<div id="dd" class="easyui-droppable" data-options="accept:'#d1,#d3'" style="width:100px;height:100px;"></div> 

<p>分页</p>
<div id="pp" class="easyui-pagination" data-options="total:2000,pageSize:10" style="background:#efefef;border:1px solid #ccc;"></div> 



<div>
	<table>
	
		<tr>
			<td>名称：</td>
			<td>服务器地址：</td>
			<td>redis-ip地址：</td>
			<td>redis-ip端口：</td>
			<td>redis-库编号：</td>
			<td>redis-密码：</td>
		</tr>
		<tr>
			<form action="ServerIP.action" method="post" onsubmit="return getnum();">
			<td style="width:150px">
				<input type="text" name="servername" value="">
			</td>
			<td style="width:150px">
				<input type="text" name="address" value="">
			</td>
			<td style="width:150px">
				<input type="text" name="redisip" value="">
			</td>
			<td style="width:150px">
				<input type="text" name="redisport" value="">
			</td>
			<td style="width:150px">
				<input id="redisnum" type="text" name="redisnum" value="">
			</td>
			<td style="width:150px">
				<input type="text" name="redispw" value="">
			</td>
			<td style="width:300px">
				<input type="hidden" name="res" value="1">
				<input type="hidden" name="iptype" value="3">
				<input type="submit" value="新增/更新">
				（根据配置名称更新）
			</td>
			</form>
		</tr>
	</table>
	<table>
		<tr>
			<td style="width:300px">
				（范例：http://172.18.7.159:8081/pass/ws）
			</td>
		</tr>
	</table>
	<!-- 
	<table>
		<tr>
			<td style="width:150px">
				<form action="ServerIP.action" method="post">
					刷新数据：<input type="submit" value="刷新">
				</form>
			</td>
		</tr>
	</table>
	 -->
	<p>-------------------------------------------------------------------------------------------------------------------</p>
	
	<table border="1">
		<tr>
			<td>
				名称
			</td>
			<td>
				服务地址
			</td>
			<td>
				redis-ip
			</td>
			<td>
				redis-端口
			</td>
			<td>
				redis-库编号
			</td>
			<td>
				redis-密码
			</td>
			<td>
				删除
			</td>
		</tr>
		<c:forEach  var="list1" items="${list}">
		<tr>
			<td>
				<c:out value="${list1.servername}"></c:out>
			</td>
			<td>
				<c:out value="${list1.address}"></c:out>
			</td>
			<td>
				<c:out value="${list1.redisip}"></c:out>
			</td>
			<td>
				<c:out value="${list1.redisport}"></c:out>
			</td>
			<td>
				<c:out value="${list1.redisnum}"></c:out>
			</td>
			<td>
				<c:out value="${list1.redispw}"></c:out>
			</td>
			
			<td>
				<c:if test="${list1.iptype >2}">
				<form action="ServerIP.action" method="post">
					<input type="hidden" name="res" value="2">
					<input type="hidden" name="id" value="${list1.id}">
					<input type="submit" value="删除">
				</form>
				</c:if>
			</td>
		</tr>
		</c:forEach>
	</table>
</div>
</div>
</body>
</html>