<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="/pass_java_anli/css/nameliebiao.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript" src="/pass_java_anli/js/jquery.js"></script>
<script type="text/javascript">
/*页面打开就能运行的JS */
var anlitype=${anlitype };
var anlistatus=${anlistatus };
if(anlitype>0){
	$(function(){
		$("#anlitype").val(anlitype);
	});
}
if(anlistatus>0){
	$(function(){
		$("#anlistatus").val(anlistatus);
	});
}

var servername1='${servername1}';
var servername2='${servername2}';
if(servername1 !='' || servername2 !=''){
	$(function(){
		$("#a5").val(servername1);
		$("#a7").val(servername2);
		$("#a8").val(servername1);
		$("#a6").val(servername2);
		
	});
}
</script>

<script type="text/javascript">
/*action返回后，才能运行的JS */
$(function(){
	var anlitype='${anlitype }';
	var anlistatus='${anlistatus }';
	$("#anlitype").val(anlitype);
	$("#anlistatus").val(anlistatus);
});

/*点击事件后，才能运行的JS */
function get_shuju(){
	var gatherbaseinfo=$("#a1").val();
	var gatherresult=$("#a2").val();
	var gatherresult_java=$("#a3").val();
	var note=$("#a4").val();
	var servername2=$("#a6").val();
	var servername1=$("#a5").val();
	
	$("#a7").val(servername2);
	$("#a8").val(servername1);
	
	$("#b1").val(gatherbaseinfo);
	$("#b2").val(gatherresult);
	$("#b3").val(gatherresult_java);
	
	
	$("#b4").val(gatherbaseinfo);
	$("#b5").val(gatherresult);
	$("#b6").val(gatherresult_java);
	
	$("#b7").val(gatherbaseinfo);
	$("#b8").val(gatherresult);
	$("#b9").val(gatherresult_java);
	
	/*保存/更新PASS-WIN数据到数据库*/
	$("#b10").val(gatherbaseinfo);
	$("#b11").val(gatherresult);
	$("#b12").val(gatherresult_java);
	$("#b13").val(note);
	$("#b14").val(note);
	$("#b15").val(note);
	
	/*
	document.from2.b1.value=a1.innerHTML;
	document.from2.b2.value=a2.innerHTML;
	document.from2.b3.value=a3.innerHTML;
	
	document.from1.b1.value=a1.innerHTML;
	document.from1.b2.value=a2.innerHTML;
	document.from1.b3.value=a3.innerHTML;
	
	document.from3.b1.value=a1.innerHTML;
	document.from3.b2.value=a2.innerHTML;
	document.from3.b3.value=a3.innerHTML;
	*/
	return true;
}

</script>
<title>测试用JSON查看</title>
</head>
<body >
<div class="bg">
	<fieldset style="width: 90%;height: 95%">
		<legend>
			<c:choose>
				<c:when test="${param.test==1}">
					模拟测试
				</c:when>
				<c:when test="${empty anliname}">
					新增案例
				</c:when>
				<c:otherwise>
					${anliname}
				</c:otherwise>
			</c:choose>
		</legend>
			<table>
				<tr>
					<td>
						<c:if test="${status=='err' }">
						<c:out value="案例数据重复，请排查"></c:out>
						</c:if>
					</td>
				</tr>
				<tr>
					<td>
						<!-- el表达式空值判断  ${empty id }-->
						<c:if test="${ param.test !=1 && (param.id==0 || id==0)}">
						<form action="Sa_gather_json.action" method="post">
						<input type="hidden" name="anlitype" value="${anlitype }">
						<input type="hidden" name="version" value="${version }">
						<input type="hidden" name="usertype" value="1">
						<input type="hidden" name="res" value="4">
						案例名称：<input type="text" name="anliname" value="${anliname }">
						<input type="submit" value="保存">(新增案例前先保存案例名称)
						</form>
						</c:if>
					</td>
				</tr>
			</table>
			<table>
				<tr>
					<td>
						<c:if test="${param.test != 1 }">
						<form action="Sa_gather_json.action" method="post" onsubmit="return get_shuju();">
						<input id="b10" type="hidden" name="gatherbaseinfo" value="${fn:escapeXml(gatherbaseinfo) }">
						<input id="b11" type="hidden" name="gatherresult" value="${fn:escapeXml(gatherresult)}">
						<input id="b12" type="hidden" name="gatherresult_java" value="${fn:escapeXml(gatherresult_java)}">
						<input id="b13" type="hidden" name="note" value="${note}">
						<input type="hidden" name="id" value="${id }">
						<input type="hidden" name="version" value="${version }">
						<input type="hidden" name="anliname" value="${anliname }">
						<input type="hidden" name="res" value="3">
						<input type="submit"  value="保存/更新PASS-WIN数据到数据库">(案例数据如有变更，请确认后再保存)
						案例类型：<select id="anlitype" name="anlitype">
							<option value="1">自动案例</option>
							<option value="2">手动案例</option>
							<option value="3">说明书案例</option>
							<option value="4">浮动窗口</option>
							<option value="5">详细信息</option>
							<option value="6">用药理由</option>
							<option value="7">右键菜单</option>
							<option value="8">模块菜单</option>
							<option value="9">未定位</option>
						</select>
						状态：<select id="anlistatus" name="anlistatus">
							<option value="0">启用</option>
							<option value="1">作废</option>
						</select>
						</form>
						</c:if>
						<!-- 
						<c:if test="${empty id}">
							<a href="Sa_gather_json.action?res=1&&gatherresult=${fn:escapeXml(gatherresult )}&&gatherresult_java=${fn:escapeXml(gatherresult_java)}" target="_blank">点击查看</a>
						</c:if>
						 -->
					</td>
				</tr>
			</table>
			<table>
				<tr>
					<td style="width:640px">
						<form action="Passwinaction.action" method="post" onsubmit="return get_shuju();">
						<input type="submit" value="PASS-WIN服务请求">
						<input type="hidden" name="test" value="${test }">
						<input type="hidden" name="req" value="1">
						<input type="hidden" name="anlitype" value="${anlitype }">
						<input type="hidden" name="anlistatus" value="${anlistatus }">
						<input type="hidden" name="version" value="${version }">
						<input type="hidden" name="id" value="${id }">
						<input type="hidden" name="anliname" value="${anliname }">
						<!--fn标签的escapeXml可以处理input转义符问题-->
						<input id="b9" type="hidden" name="gatherresult_java" value="${fn:escapeXml(gatherresult_java)} ">
						<input id="b8" type="hidden" name="gatherresult" value="${fn:escapeXml(gatherresult )}">
						<input id="b7" type="hidden" name="gatherbaseinfo" value="${fn:escapeXml(gatherbaseinfo)} ">
						<input id="b14" type="hidden" name="note" value="${note}">
						<c:choose>
							<c:when test="${test==1 }">
								<input type="hidden" name="url" value="${url }">
								pass-win服务地址：<input type="text" name="winurl" value="${winurl }"><br>
								(范例：http://172.18.7.154/PASS4WebService/PASSwebService.asmx/Mc_DoScreen)<br>
								(范例：http://172.18.7.154/PASS4WebService/PASSwebService.asmx/Mc_DoQuery)<br>
								(范例：http://172.18.7.154/PASS4WebService/PASSwebService.asmx/Mc_DoDetail)<br>
								(范例：http://172.18.7.154/PASS4WebService/PASSwebService.asmx/Mc_DoReason)<br>
								(范例：http://172.18.7.154/PASS4WebService/PASSwebService.asmx/Mc_DoModule)<br>
							</c:when>
							<c:otherwise>
								测试地址：
								<select id="a5" name="servername1">
									<c:forEach begin="0" items="${iplist }" var="iplist1">
										<c:if test="${iplist1.iptype ==1 }">
											<option value="${iplist1.servername}">${iplist1.servername}</option>
										</c:if>
									</c:forEach>
								</select>
								<input id="a7" type="hidden" name="servername2" value="${servername2 }">
							</c:otherwise>
						</c:choose>
						</form>
					</td>
					<td>
						<form action="Passwinaction.action" method="post"  onsubmit="return get_shuju();">
						<input type="hidden" name="req" value="2">
						<input type="submit" value="PASS-JAVA服务请求">
						<input type="hidden" name="anlitype" value="${anlitype }">
						<input type="hidden" name="anlistatus" value="${anlistatus }">
						<input type="hidden" name="version" value="${version }">
						<input type="hidden" name="test" value="${test }">
						<input type="hidden" name="id" value="${id }">
						<input type="hidden" name="anliname" value="${anliname }">
						<!--fn标签的escapeXml可以处理input转义符问题-->
						<input id="b6" type="hidden" name="gatherresult_java" value="${fn:escapeXml(gatherresult_java)} ">
						<input id="b5" type="hidden" name="gatherresult" value="${fn:escapeXml(gatherresult )}">
						<input id="b4" type="hidden" name="gatherbaseinfo" value="${fn:escapeXml(gatherbaseinfo)}">
						<input id="b15" type="hidden" name="note" value="${note}">
						<c:choose>
							<c:when test="${test==1 }">
								<input type="hidden" name="winurl" value="${winurl }">
								pass-java服务地址：<input type="text" name="url" value="${url }"><br>
								(范例：http://172.18.7.159:8081/pass/ws/PASSwebService.asmx/Mc_DoScreen)<br>
								(范例：http://172.18.7.159:8081/pass/ws/PASSwebService.asmx/Mc_DoQuery)<br>
								(范例：http://172.18.7.159:8081/pass/ws/PASSwebService.asmx/Mc_DoDetail)<br>
								(范例：http://172.18.7.159:8081/pass/ws/PASSwebService.asmx/Mc_DoReason)<br>
								(范例：http://172.18.7.159:8081/pass/ws/PASSwebService.asmx/Mc_DoModule)<br>
							</c:when>
							<c:otherwise>
								测试地址：
								<select id="a6" name="servername2">
									<c:forEach begin="0" items="${iplist }" var="iplist1">
										<c:if test="${iplist1.iptype >1 }">
											<option value="${iplist1.servername}">${iplist1.servername}</option>
										</c:if>
									</c:forEach>
								</select>
								<input id="a8" type="hidden" name="servername1" value="${servername1 }">
							</c:otherwise>
						</c:choose>
						</form>
					</td>
				</tr>
				<tr>
					<td>
						<form action="Sa_gather_json.action" method="post"  target="_blank" onsubmit="return get_shuju();">
						<input type="hidden" name="res" value="5">
						<input type="submit" value="查看对比结果">
						<!--<input type="hidden" name="test" value="1">-->
						<!--fn标签的escapeXml可以处理input转义符问题-->
						<input type="hidden" name="test" value="${test }">
						<input type="hidden" name="url" value="${url }">
						<input type="hidden" name="id" value="${id }">
						<input type="hidden" name="anliname" value="${anliname }">
						<input type="hidden" name="anlitype" value="${anlitype }">
						<input type="hidden" name="version" value="${version }">
						<input id="b3" type="hidden" name="gatherresult_java" value="${fn:escapeXml(gatherresult_java)} ">
						<input id="b2" type="hidden" name="gatherresult" value="${fn:escapeXml(gatherresult )}">
						<input id="b1" type="hidden" name="gatherbaseinfo" value="${fn:escapeXml(gatherbaseinfo)} ">
						</form>
					</td>
				</tr>
				<tr>
					<td>
						<c:out value="${err }"></c:out>
						<c:out value="${message1}"></c:out>
						<c:out value="${message }"></c:out>
					</td>
				</tr>
			</table>
			<table>
				<tr>
					<td >
						请求JSON
						<textarea id="a1" style="width:600px;height:200px;"><c:out value="${gatherbaseinfo }"></c:out></textarea>
					</td>
					<td>
						案例备注
						<textarea id="a4" style="width:600px;height:100px;"><c:out value="${note }"></c:out></textarea>
					</td>
				</tr>
				<tr>
					<td>
						断言JSON
						<textarea id="a2"  type="text" style="width:600px;height:280px;"><c:out value="${gatherresult }"></c:out></textarea>
					</td>
					<td>
						响应JSON
						<textarea id="a3" type="text"  style="width:600px;height:280px;"><c:out value="${gatherresult_java }"></c:out></textarea>
					</td>
				</tr>
			</table>
	</fieldset>
</div>
</body>
</html>