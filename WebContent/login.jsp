<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="/pass_java_anli/css/login.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript" src="/pass_java_anli/js/jquery.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	//$("dd").hide();
	//$("li").hide();
	$("dt").click(function(){
	//	$("dd").slideUp(); 
	//	$(this).parent().find("dd").slideToggle();
	//	$("li").hide();
	$("dt").attr("style","width:90%;height:20px;background-color:#6cbffd;margin-top:10px")
	$(this).attr("style","width:90%;height:20px;background-color:#ff80c0;margin-top:10px");
	});
	//$("#1 dd").click(function(){
	//	$(this).parent().find("li").slideToggle();
	//});
});
</script>
<title>坚果-${param.version} 整合测试用例</title>
</head>
<body>
<div class="bg">
<div class="leftbox">

<!-- 
<div class="anniu">
<a href="/pcdemo/rightjsp/jibenxinxi1.jsp" target="frightbox" style="text-decoration: none">查询案例类型</a>
</div>
<div class="anniu1">
<a href="/pcdemo/rightjsp/kongbai.jsp" target="frightbox" style="text-decoration: none">主页</a>
</div>

<div class="anniu2">
<form action="index.action" method="post">
<input type="hidden" name="hiscode" value="${hiscode }">
<input type="hidden" name="doctorname" value="${doctorname }">
<input type="submit" value="查询案例类型">
</form>
</div>
<div class="anniu3">
<form action="index.action" method="post">
<input type="hidden" name="hiscode" value="-1">
<input type="hidden" name="doctorname" value="">
<input type="submit" value="注销" >
</form>
</div>
<br>
 -->

<table>
	<tr>
		<td><a href="${pageContext.request.contextPath}/dao.jsp?version=${version }" target="_blank" >系统功能</td>
		<td><a href="${pageContext.request.contextPath}/index.jsp"  >退出</td>
	</tr>
	<tr>
		<td><a href="Sa_gather_json.action?res=6&test=1&version=${version }" target="_blank">模拟测试</a></td>
	</tr>
	<tr>
		<td><font size="3" color="#ff8040" >案例类型列表</font></td>
		<td><font size="3" color="#ff8040" >版本号${version }</font></td>
	</tr>
</table>
	<dl><!-- 
		<c:forEach begin="0"  items="${list}" var="list1">
		<c:if test="${list1.anlitype=='1'}">
		<dt style="width:90%;height:20px;background-color:#6cbffd;margin-top:10px">
			<a style="text-decoration:none;" href="Anliaction.action?anlitype=${list1.anlitype}&&res=1" target="tbox"><c:out value="自动案例" ></c:out></a>
		</dt>
		</c:if>
		<c:if test="${list1.anlitype=='2'}">
		<dt style="width:90%;height:20px;background-color:#6cbffd;margin-top:10px">
			<a style="text-decoration:none;" href="Anliaction.action?anlitype=${list1.anlitype}&&res=1" target="tbox"><c:out value="手动案例"></c:out></a>
		</dt>
		</c:if>
		<c:if test="${list1.anlitype=='3'}">
		<dt style="width:90%;height:20px;background-color:#6cbffd;margin-top:10px">
			<a style="text-decoration:none;" href="Anliaction.action?anlitype=${list1.anlitype}&&res=1" target="tbox"><c:out value="说明书案例"></c:out></a>
		</dt>
		</c:if>
		<c:if test="${list1.anlitype =='4'}">
		<dt style="width:90%;height:20px;background-color:#6cbffd;margin-top:10px">
			<a style="text-decoration:none;" href="Anliaction.action?anlitype=${list1.anlitype}&&res=1" target="tbox"><c:out value="浮动窗口"></c:out></a>
		</dt>
		</c:if>
		<c:if test="${list1.anlitype >'4'}">
		<dt style="width:90%;height:20px;background-color:#6cbffd;margin-top:10px">
			<a style="text-decoration:none;" href="Anliaction.action?anlitype=${list1.anlitype}&&res=1" target="tbox"><c:out value="未定位"></c:out></a>
		</dt>
		</c:if>
		</c:forEach>
		 -->
		 <dt style="width:90%;height:20px;background-color:#6cbffd;margin-top:10px">
			<a style="text-decoration:none;" href="Anliaction.action?anlitype=1&&res=1&&version=${version }" target="tbox"><c:out value="自动案例" ></c:out></a>
		</dt>
		<dt style="width:90%;height:20px;background-color:#6cbffd;margin-top:10px">
			<a style="text-decoration:none;" href="Anliaction.action?anlitype=2&&res=1&&version=${version }" target="tbox"><c:out value="手动案例"></c:out></a>
		</dt>
		<dt style="width:90%;height:20px;background-color:#6cbffd;margin-top:10px">
			<a style="text-decoration:none;" href="Anliaction.action?anlitype=3&&res=1&&version=${version }" target="tbox"><c:out value="说明书案例"></c:out></a>
		</dt>
		<dt style="width:90%;height:20px;background-color:#6cbffd;margin-top:10px">
			<a style="text-decoration:none;" href="Anliaction.action?anlitype=4&&res=1&&version=${version }" target="tbox"><c:out value="浮动窗口"></c:out></a>
		</dt>
		<dt style="width:90%;height:20px;background-color:#6cbffd;margin-top:10px">
			<a style="text-decoration:none;" href="Anliaction.action?anlitype=5&&res=1&&version=${version }" target="tbox"><c:out value="详细信息"></c:out></a>
		</dt>
		<dt style="width:90%;height:20px;background-color:#6cbffd;margin-top:10px">
			<a style="text-decoration:none;" href="Anliaction.action?anlitype=6&&res=1&&version=${version }" target="tbox"><c:out value="用药理由"></c:out></a>
		</dt>
		<dt style="width:90%;height:20px;background-color:#6cbffd;margin-top:10px">
			<a style="text-decoration:none;" href="Anliaction.action?anlitype=7&&res=1&&version=${version }" target="tbox"><c:out value="右键菜单"></c:out></a>
		</dt>
		<dt style="width:90%;height:20px;background-color:#6cbffd;margin-top:10px">
			<a style="text-decoration:none;" href="Anliaction.action?anlitype=8&&res=1&&version=${version }" target="tbox"><c:out value="模块菜单"></c:out></a>
		</dt>
		<dt style="width:90%;height:20px;background-color:#6cbffd;margin-top:10px">
			<a style="text-decoration:none;" href="Anliaction.action?anlitype=9&&res=1&&version=${version }" target="tbox"><c:out value="未定位"></c:out></a>
		</dt>
	</dl>
</div>
<div class="rightbox">
<iframe src="" name="tbox" width="100%" height="100%" frameborder="0"></iframe>
</div>
</div>
</body>
</html>