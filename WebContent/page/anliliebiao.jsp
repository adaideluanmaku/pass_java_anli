<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="/pass_java_anli/js/jquery.js"></script>
<script type="text/javascript">

</script>
<script type="text/javascript">
$(function(){
	var servername='${servername }';
	if(servername != ""){
		$("#a1").val(servername);
	}
});
function get_address(){
	var servername1=$("#a1").val();
	$("#b1").val(servername1);
	$("#c1").val(servername1);
	$("#c2").val(servername1);
	$("#c3").val(servername1);
	$("#c4").val(servername1);
	$("#c5").val(servername1);
	$("#c6").val(servername1);
	$("#c7").val(servername1);
	$("#c8").val(servername1);
	$("#c9").val(servername1);
	$("#c10").val(servername1);
	$("#c11").val(servername1);
	$("#c12").val(servername1);
	$("#c13").val(servername1);
	$("#c14").val(servername1);
	$("#c15").val(servername1);
	return true;
}
</script>
<link rel="stylesheet" type="text/css" href="/pass_java_anli/css/nameliebiao.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>错误案例</title>
</head>
<body>
<div class="bg">
	<div style="width:1200px;">
	<table>
		<tr >
			<td>
				<form action="Anliaction.action" method="post">
				<input type="hidden" name="res" value="2">
				<input type="hidden" name="anlitype" value="${anlitype }">
				<input type="hidden" name="version" value="${version }">
				案例名称查询：<input type="text" name="anliname" value="${anliname }">
				<input type="submit" value="案例查询">
				</form>
			</td>
			<td>
				<form action="Anliaction.action" method="post">
				<input type="hidden" name="res" value="2">
				<input type="hidden" name="anlitype" value="${anlitype }">
				<input type="hidden" name="anliname" value="${anliname }">
				<input type="hidden" name="version" value="${version }">
				<input type="hidden" name="paixu" value="1">
				<input type="submit" value="问题靠前排序">
				</form>
			</td>
			<td>
				测试地址：
				<select id="a1">
					<c:forEach begin="0" items="${iplist }" var="iplist1">
						<c:if test="${iplist1.iptype >1 }">
							<option value="${iplist1.servername}">${iplist1.servername}</option>
						</c:if>
					</c:forEach>
				</select>
			</td>
			<td>
				<form action="Json_err.action" method="post" onsubmit="return get_address();">
					<input type="hidden" name="anlitype" value="${anlitype }">
					<input type="hidden" name="anliname" value="${anliname }">
					<input id="b1" type="hidden" name="servername" value="${servername }">
					<input type="hidden" name="count" value="${count }">
					<input type="hidden" name="req" value="2">
					<input type="hidden" name="paixu" value="${paixu}">
					<input type="hidden" name="version" value="${version }">
					<input type="submit" value="审查全部测试">
				</form>
			</td>
			<td>
				<a href="Sa_gather_json.action?id=0&&anlitype=${anlitype}&&version=${version}&&anliname=新增案例" target="_blank">增加案例</a>
			</td>
			<td>
				<c:if test="${messeage==1}">
					<font color="red"><c:out value="全部案例测试结束,错误总数是:${errcount }"></c:out></font>
				</c:if>
				<c:if test="${messeage==2}">
					<font color="red"><c:out value="${anliname1 }-测试结束"></c:out></font>
				</c:if>
			</td>
		</tr>
		<c:if test="${version==1609 && anlitype==1}">
		<tr>
			<td>
				<font color="red">1609-剂量范围编号"剂量范围468"以上案例忽略</font>
			</td>
		</tr>
		</c:if>
	</table>
	</div>
	<!-- 列表功能 -->
	<div>
		<fieldset>
		<legend>案例列表</legend>
			<table border="1">
				<tr>
					<td>序列号</td>
					<td>唯一编号</td>
					<td>模块名称</td>
					<td>案例名称</td>
					<td>查看gather_log</td>
					<td>查看对比结果</td>
					<td>点击测试</td>
					<td>测试结果</td>
					<td>version</td>
					<td>删除案例</td>
					<td>刷新redis</td>
					<td>备注</td>
					<td>状态</td>
				</tr>
				<c:forEach begin="0" items="${list }" var="list1" varStatus="a">
					<tr>
						<td><c:out value="${(page-1)*15+a.count }"></c:out></td>
						<td><c:out value="${list1.id }"></c:out></td>
						<td><c:out value="${list1.modulename }"></c:out></td>
						<td title="${list1.anliname }" ><c:out value="${list1.anliname }"></c:out></td>
						<td>
							<a href="Sa_gather_json.action?id=${list1.id }&&version=${version }&&anlitype=${list1.anlitype }&&anliname=${list1.anliname}&&res=2" target="_blank">点击查看</a>
						</td>
						<td>
							<a href="Sa_gather_json.action?id=${list1.id }&&version=${version }&&anliname=${list1.anliname}&&res=1" target="_blank">点击查看</a>
						</td>
						<td>
							<form action="Json_err.action" method="post" onsubmit="return get_address();">
								<input id="c${a.count }" type="hidden" name="servername" value="${servername }">
								<input type="hidden" name="page" value="${page }">
								<input type="hidden" name="zpage" value="${zpage }">
								<input type="hidden" name="anliname" value="${anliname }">
								<input type="hidden" name="anlitype" value="${anlitype }">
								<input type="hidden" name="id" value="${list1.id }">
								<input type="hidden" name="count" value="${count }">
								<input type="hidden" name="paixu" value="${paixu}">
								<input type="hidden" name="version" value="${version }">
								<input type="hidden" name="anliname1" value="${list1.anliname }">
								<input type="hidden" name="anlistatus" value="${list1.anlistatus }">
								<input type="hidden" name="req" value="1">
								<input type="submit" value="测试">
							</form>
						</td>
						<td>
							<c:if test="${list1.state==1}">
								<c:out value="错误"></c:out>
							</c:if>
						</td>
						<td>
							<c:out value="${list1.version }"></c:out>
						</td>
						<td>
							<form action="Json_err.action" method="post">
								<input type="hidden" name="page" value="${page }">
								<input type="hidden" name="zpage" value="${zpage }">
								<input type="hidden" name="anliname" value="${anliname }">
								<input type="hidden" name="anliname1" value="${list1.anliname }">
								<input type="hidden" name="anlitype" value="${anlitype }">
								<input type="hidden" name="id" value="${list1.id }">
								<input type="hidden" name="count" value="${count }">
								<input type="hidden" name="paixu" value="${paixu}">
								<input type="hidden" name="version" value="${version }">
								<input type="hidden" name="req" value="3">
								<input type="submit" value="删除">
							</form>
						</td>
						<td>
							<c:if test="${!empty list1.tablename }">
								<form action="Json_err.action" method="post">
									<input type="hidden" name="page" value="${page }">
									<input type="hidden" name="zpage" value="${zpage }">
									<input type="hidden" name="anliname" value="${anliname }">
									<input type="hidden" name="anlitype" value="${anlitype }">
									<input type="hidden" name="redisanliname" value="${list1.anliname }">
									<input type="hidden" name="count" value="${count }">
									<input type="hidden" name="paixu" value="${paixu}">
									<input type="hidden" name="version" value="${version }">
									<input type="hidden" name="req" value="4">
									<input type="submit" value="提交">
								</form>
							</c:if>
						</td>
						<td title="${list1.note}" style="width:100px">
							<c:out value="${fn:substring(list1.note, 0, 5)}" ></c:out>
						</td>
						<td style="width:100px">
							<c:if test="${list1.anlistatus==1}">
								<c:out value="作废" ></c:out>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</table>
		</fieldset>
	</div>
	<!-- 分页功能 -->
	<div>
		<table>
			<tr>
			<c:if test="${zpage>=9 }">
				<td>
				<form action="Anliaction.action" method="post">
				<input type="hidden" name="res" value="2">
				<input type="hidden" name="anliname" value="${anliname }">
				<input type="hidden" name="anlitype" value="${anlitype }">
				<input type="hidden"  name="page" value="${page-1 }">
				<input type="hidden" name="zpage" value="${zpage }">
				<input type="hidden" name="count" value="${count }">
				<input type="hidden" name="version" value="${version }">
				<input type="hidden" name="paixu" value="${paixu}">
				<input type="submit" value="上一页" >
				</form>
				</td>
				<c:if test="${page<=7 }">
					<c:forEach begin="1" end="9" var="pages" varStatus="yes">
					<td  width="40px">
					<form action="Anliaction.action" method="post">
					<input type="hidden" name="res" value="2">
					<input type="hidden" name="anliname" value="${anliname }">
					<input type="hidden" name="zpage" value="${zpage }">
					<input type="hidden" name="anlitype" value="${anlitype }">
					<input type="hidden" name="version" value="${version }">
					<input type="hidden" name="count" value="${count }">
					<input type="submit" name="page" value="${pages}">
					<input type="hidden" name="paixu" value="${paixu}">
					</form>
					</td>
					</c:forEach>
				</c:if>
				<c:if test="${page>7 && zpage-page>=4}">
					<c:forEach begin="${page-4 }" end="${page+4 }" var="pages" varStatus="yes">
					<td width="40px">
					<form action="Anliaction.action" method="post">
					<input type="hidden" name="res" value="2">
					<input type="hidden" name="anliname" value="${anliname }">
					<input type="hidden" name="zpage" value="${zpage }">
					<input type="hidden" name="anlitype" value="${anlitype }">
					<input type="hidden" name="version" value="${version }">
					<input type="hidden" name="count" value="${count }">
					<input type="submit" name="page" value="${pages}">
					<input type="hidden" name="paixu" value="${paixu}">
					</form>
					</td>
					</c:forEach>
				</c:if>
				<c:if test="${page>7 && zpage-page<4}">
					<c:forEach begin="${page-4 }" end="${zpage }" var="pages">
					<td width="40px">
					<form action="Anliaction.action" method="post">
					<input type="hidden" name="res" value="2">
					<input type="hidden" name="anliname" value="${anliname }">
					<input type="hidden" name="zpage" value="${zpage }">
					<input type="hidden" name="anlitype" value="${anlitype }">
					<input type="hidden" name="version" value="${version }">
					<input type="hidden" name="count" value="${count }">
					<input type="submit" name="page" value="${pages}">
					<input type="hidden" name="paixu" value="${paixu}">
					</form>
					</td>
					</c:forEach>
				</c:if>
				<td>
				<form action="Anliaction.action" method="post">
				<input type="hidden" name="res" value="2">
				<input type="hidden" name="anliname" value="${anliname }">
				<input type="hidden" name="anlitype" value="${anlitype }">
				<input type="hidden" name="version" value="${version }">
				<input type="hidden" name="zpage" value="${zpage }">
				<input type="hidden" value="${page+1 }"  name="page">
				<input type="hidden" name="count" value="${count }">
				<input type="hidden" name="paixu" value="${paixu}">
				<input type="submit" value="下一页" >
				</form>
				</td>
				<td><c:out value="当前页是：${page };"></c:out></td>
				<td><c:out value="总页数是：${zpage };"></c:out></td>
				<td><c:out value="总数是：${count }"></c:out></td>
			</c:if>
				
			<c:if test="${zpage<9 }">
				<td>
					<form action="Anliaction.action" method="post">
					<input type="hidden" name="res" value="2">
					<input type="hidden" name="anliname" value="${anliname }">
					<input type="hidden" name="anlitype" value="${anlitype }">
					<input type="hidden" name="version" value="${version }">
					<input type="hidden" value="${page-1 }"  name="page">
					<input type="hidden" name="zpage" value="${zpage }">
					<input type="hidden" name="count" value="${count }">
					<input type="hidden" name="paixu" value="${paixu}">
					<input type="submit" value="上一页" >
					</form>
				</td>
					<c:forEach begin="1" end="${zpage }" var="pages" varStatus="yes">
					<td>
					<form action="Anliaction.action" method="post">
					<input type="hidden" name="res" value="2">
					<input type="hidden" name="anliname" value="${anliname }">
					<input type="hidden" name="zpage" value="${zpage }">
					<input type="hidden" name="version" value="${version }">
					<input type="hidden" name="anlitype" value="${anlitype }">
					<input type="hidden" name="count" value="${count }">
					<input type="submit" name="page" value="${pages}">
					<input type="hidden" name="paixu" value="${paixu}">
					</form>
				</td>
				</c:forEach>
				<td>
					<form action="Anliaction.action" method="post">
					<input type="hidden" name="res" value="2">
					<input type="hidden" name="anliname" value="${anliname }">
					<input type="hidden" name="anlitype" value="${anlitype }">
					<input type="hidden" name="version" value="${version }">
					<input type="hidden" name="zpage" value="${zpage }">
					<input type="hidden"  name="page" value="${page+1 }">
					<input type="hidden" name="count" value="${count }">
					<input type="hidden" name="paixu" value="${paixu}">
					<input type="submit" value="下一页" >
					</form>
				</td>
				<td><c:out value="当前页是：${page };"></c:out></td>
				<td><c:out value="总页数是：${zpage };"></c:out></td>
				<td><c:out value="总数是：${count }"></c:out></td>
			</c:if>
			</tr>
		</table>
	</div>
</div>
</body>
</html>