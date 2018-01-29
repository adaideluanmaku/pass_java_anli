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
var servername='${servername}';
if(servername !=''){
	$(function(){
		$("#a1").val(servername);
	});
}

$(function(){
	var selv=${tabletype };
	//alert(selv);
	$("#sel").val(selv);
});

function get_shuju(){
	var servername=$("#a1").val();
	$("#b1").attr("value",servername);
	$("#b2").attr("value",servername);
	$("#b3").attr("value",servername);
	$("#b4").attr("value",servername);
	$("#b5").attr("value",servername);
	$("#b6").attr("value",servername);
	$("#b7").attr("value",servername);
	$("#b8").attr("value",servername);
	$("#b9").attr("value",servername);
	$("#b10").attr("value",servername);
	$("#b11").attr("value",servername);
	$("#b12").attr("value",servername);
	$("#b13").attr("value",servername);
	$("#b14").attr("value",servername);
	$("#b15").attr("value",servername);
	$("#b16").attr("value",servername);
	$("#b17").attr("value",servername);
	$("#b18").attr("value",servername);
	$("#b19").attr("value",servername);
	$("#b20").attr("value",servername);
	$("#b21").attr("value",servername);
	$("#b22").attr("value",servername);
	$("#b23").attr("value",servername);
	$("#b24").attr("value",servername);
	$("#b25").attr("value",servername);
	$("#b26").attr("value",servername);
	$("#b27").attr("value",servername);
	$("#b28").attr("value",servername);
	$("#b29").attr("value",servername);
	$("#b30").attr("value",servername);
	$("#b31").attr("value",servername);
	$("#b32").attr("value",servername);
	$("#b33").attr("value",servername);
	$("#b34").attr("value",servername);
	$("#b35").attr("value",servername);
	$("#b36").attr("value",servername);
	$("#b37").attr("value",servername);
	$("#b38").attr("value",servername);
	
	return true;
}
</script>
<title>坚果-整合测试用例</title>
</head>
<body>
<div class="bg">
<div class="redis1">
<table>
	<tr>
		<td style="width:200px">
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
				<select name="tabletype" id="sel">
					<option value="1">字典表</option>
					<option value="2">自定义表</option>
					<option value="3">屏蔽表</option>
				</select>
				<input id="b1" type="hidden" name="servername" value="${servername }">
				<input type="submit" value="切换">
			</form>
		</td>
		<td style="width:200px">
			测试地址：
			<select id="a1">
				<c:forEach begin="0" items="${iplist }" var="iplist1">
					<c:if test="${iplist1.iptype >1 }">
						<option value="${iplist1.servername}">${iplist1.servername}</option>
					</c:if>
				</c:forEach>
			</select>
		</td>
	</tr>
</table>
	<c:if test="${tabletype==1 }">
	<table>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_dict_allergen查询">
			</td>
			<td>
				match_scheme:<input type="text" name="match_scheme" value="${match_scheme }">
			</td>
			<td>
				allercode:<input type="text" name="allercode" value="${allercode }">
			</td>
			<input type="hidden" name="req" value="1">
			<input id="b2" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_dict_drug查询">
			</td>
			<td>
				match_scheme:<input type="text" name="match_scheme" value="${match_scheme }">
			</td>
			<td>
				drugcode:<input type="text" name="drugcode" value="${drugcode }">
			</td>
			<input type="hidden" name="req" value="2">
			<input id="b3" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();" >
			<td>
				<input type="submit" value="mc_dict_disease查询">
			</td>
			<td>
				match_scheme:<input type="text" name="match_scheme" value="${match_scheme }">
			</td>
			<td>
				discode:<input type="text" name="discode" value="${discode }">
			</td>
			<input type="hidden" name="req" value="3">
			<input id="b4" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_cost_dose_unit查询">
			</td>
			<td>
				match_scheme:<input type="text" name="match_scheme" value="${match_scheme }">
			</td>
			<td>
				drugcode:<input type="text" name="drugcode" value="${drugcode }">
			</td>
			<td>
				drugspec:<input type="text" name="drugspec" value="${drugspec }">
			</td>
			<td>
				doseunit:<input type="text" name="doseunit" value="${doseunit }">
			</td>
			<td>
				costunit:<input type="text" name="costunit" value="${costunit }">
			</td>
			<input type="hidden" name="req" value="4">
			<input id="b5" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_dict_frequency查询">
			</td>
			<td>
				match_scheme:<input type="text" name="match_scheme" value="${match_scheme }">
			</td>
			<td>
				frequency:<input type="text" name="frequency" value="${frequency }">
			</td>
			<input type="hidden" name="req" value="5">
			<input id="b6" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_hospital查询">
			</td>
			<td>
				hiscode_user:<input type="text" name="hiscode_user" value="${hiscode_user }">
			</td>
			<input type="hidden" name="req" value="6">
			<input id="b7" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_dict_operation查询">
			</td>
			<td>
				match_scheme:<input type="text" name="match_scheme" value="${match_scheme }">
			</td>
			<td>
				operationcode:<input type="text" name="operationcode" value="${operationcode }">
			</td>
			<input type="hidden" name="req" value="7">
			<input id="b8" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_pro_drug_reason查询">
			</td>
			<td>
				caseid:<input type="text" name="caseid" value="${caseid }">
			</td>
			<td>
				recipno:<input type="text" name="recipno" value="${recipno }">
			</td>
			<td>
				orderDeptCode:<input type="text" name="orderDeptCode" value="${orderDeptCode }">
			</td>
			<td>
				orderDoctorCode:<input type="text" name="orderDoctorCode" value="${orderDoctorCode }">
			</td>
			<td>
				drugUniqueCode:<input type="text" name="druguniquecode" value="${druguniquecode }">
			</td>
			<td>
				moduleId:<input type="text" name="moduleId" value="${moduleId }">
			</td>
			<input type="hidden" name="req" value="8">
			<input id="b9" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_dict_route查询">
			</td>
			<td>
				match_scheme:<input type="text" name="match_scheme" value="${match_scheme }">
			</td>
			<td>
				routecode:<input type="text" name="routecode" value="${routecode }">
			</td>
			<input type="hidden" name="req" value="9">
			<input id="b10" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_config查询">
			</td>
			<td>
				paraname:<input type="text" name="paraname" value="${paraname }">
			</td>
			<input type="hidden" name="req" value="10">
			<input id="b11" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_dict_disease查询-PA属性">
			</td>
			<td>
				match_scheme:<input type="text" name="match_scheme" value="${match_scheme }">
			</td>
			<td>
				discode:<input type="text" name="disode" value="${disode }">
			</td>
			<input type="hidden" name="req" value="11">
			<input id="b12" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_dict_doctor查询">
			</td>
			<td>
				match_scheme:<input type="text" name="match_scheme" value="${match_scheme }">
			</td>
			<td>
				Doctorcode:<input type="text" name="Doctorcode" value="${Doctorcode }">
			</td>
			<input type="hidden" name="req" value="12">
			<input id="b13" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_dict_drug_pass查询">
			</td>
			<td>
				match_scheme:<input type="text" name="match_scheme" value="${match_scheme }">
			</td>
			<td>
				druguniquecode:<input type="text" name="druguniquecode" value="${druguniquecode }">
			</td>
			<input type="hidden" name="req" value="13">
			<input id="b14" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_hospital_match_relation查询">
			</td>
			<td>
				hiscode:<input type="text" name="hiscode" value="${hiscode }">
			</td>
			<input type="hidden" name="req" value="14">
			<input id="b15" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_params查询">
			</td>
			<td>
				checkmode:<input type="text" name="checkmode" value="${checkmode }">
			</td>
			<input type="hidden" name="req" value="15">
			<input id="b16" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_dict_route-查询-PA属性">
			</td>
			<td>
				match_scheme:<input type="text" name="match_scheme" value="${match_scheme }">
			</td>
			<td>
				routecode:<input type="text" name="routecode" value="${routecode }">
			</td>
			<input type="hidden" name="req" value="16">
			<input id="b17" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
	</table>
	</c:if>
	
	<c:if test="${tabletype==2 }">
	<table>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_user_adult查询">
			</td>
			<td>
				hiscode:<input type="text" name="hiscode" value="${hiscode }">
			</td>
			<td>
				druguniquecode:<input type="text" name="druguniquecode" value="${druguniquecode }">
			</td>
			<input type="hidden" name="req" value="17">
			<input id="b18" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_user_bacresis查询"><br>
			</td>
			<td>
				hiscode:<input type="text" name="hiscode" value="${hiscode }">
			</td>
			<td>
				druguniquecode:<input type="text" name="druguniquecode" value="${druguniquecode }">
			</td>
			<input type="hidden" name="req" value="18">
			<input id="b19" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
			<td>
			关联mc_user_match_whonet
			</td>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_user_brief查询">
			</td>
			<td>
				hiscode:<input type="text" name="hiscode" value="${hiscode }">
			</td>
			<td>
				druguniquecode:<input type="text" name="druguniquecode" value="${druguniquecode }">
			</td>
			<input type="hidden" name="req" value="19">
			<input id="b20" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_user_doctor_priv查询">
			</td>
			<td>
				hiscode:<input type="text" name="hiscode" value="${hiscode }">
			</td>
			<td>
				Doctorcode:<input type="text" name="Doctorcode" value="${Doctorcode }">
			</td>
			<td>
				druguniquecode:<input type="text" name="druguniquecode" value="${druguniquecode }">
			</td>
			<input type="hidden" name="req" value="20">
			<input id="b21" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_user_dosage查询">
			</td>
			<td>
				hiscode:<input type="text" name="hiscode" value="${hiscode }">
			</td>
			<td>
				druguniquecode:<input type="text" name="druguniquecode" value="${druguniquecode }">
			</td>
			<td>
				doseunit:<input type="text" name="doseunit" value="${doseunit }">
			</td>
			<td>
				routecode:<input type="text" name="routecode" value="${routecode }">
			</td>
			<input type="hidden" name="req" value="21">
			<input id="b22" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_user_drug_dis查询">
			</td>
			<td>moduleid:
				<select name="moduleid" style="height: 24px;width: 150px">
					<option value="7">7</option>
					<option value="8">8</option>
					<option value="9">9</option>
				</select>
			</td>
			<td>
				hiscode:<input type="text" name="hiscode" value="${hiscode }">
			</td>
			<td>
				druguniquecode:<input type="text" name="druguniquecode" value="${druguniquecode }">
			</td>
			<input type="hidden" name="req" value="22">
			<input id="b23" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_user_druglevel查询">
			</td>
			<td>
				hiscode:<input type="text" name="hiscode" value="${hiscode }">
			</td>
			<td>
				Druguniquecodeone:<input type="text" name="Druguniquecodeone" value="${Druguniquecodeone }">
			</td>
			<td>
				doseunitone:<input type="text" name="doseunitone" value="${doseunitone }">
			</td>
			<td>
				Druguniquecodetwo:<input type="text" name="Druguniquecodetwo" value="${Druguniquecodetwo }">
			</td>
			<td>
				doseunittwo:<input type="text" name="doseunittwo" value="${doseunittwo }">
			</td>
			<td>
				routecode:<input type="text" name="routecode" value="${routecode }">
			</td>
			<input type="hidden" name="req" value="23">
			<input id="b24" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_user_duptherapy查询">
			</td>
			<td>
				dupcid:<input type="text" name="dupcid" value="${dupcid }">
			</td>
			<input type="hidden" name="req" value="24">
			<input id="b25" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
			<td>
			user_max>0的数据
			</td>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_user_hepdos查询">
			</td>
			<td>
				hiscode:<input type="text" name="hiscode" value="${hiscode }">
			</td>
			<td>
				druguniquecode:<input type="text" name="druguniquecode" value="${druguniquecode }">
			</td>
			<td>
				doseunit:<input type="text" name="doseunit" value="${doseunit }">
			</td>
			<td>
				routecode:<input type="text" name="routecode" value="${routecode }">
			</td>
			<td>
				heplabel:<input type="text" name="heplabel" value="${heplabel }">
			</td>
			<input type="hidden" name="req" value="25">
			<input id="b26" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_user_inter查询">
			</td>
			<td>
				hiscode:<input type="text" name="hiscode" value="${hiscode }">
			</td>
			<td>
				Druguniquecodeone:<input type="text" name="Druguniquecodeone" value="${Druguniquecodeone }">
			</td>
			<td>
				Druguniquecodetwo:<input type="text" name="Druguniquecodetwo" value="${Druguniquecodetwo }">
			</td>
			<input type="hidden" name="req" value="26">
			<input id="b27" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_user_iv查询">
			</td>
			<td>
				hiscode:<input type="text" name="hiscode" value="${hiscode }">
			</td>
			<td>
				druguniquecode:<input type="text" name="druguniquecode" value="${druguniquecode }">
			</td>
			<td>
				routetype:<input type="text" name="routetype" value="${routetype }">
			</td>
			<input type="hidden" name="req" value="27">
			<input id="b28" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_pro_drug_advice查询">
			</td>
			<td>
				caseid:<input type="text" name="caseid" value="${caseid }">
			</td>
			<input type="hidden" name="req" value="28">
			<input id="b29" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_pro_drug_operation查询">
			</td>
			<td>
				caseid:<input type="text" name="caseid" value="${caseid }">
			</td>
			<td>
				operationcode:<input type="text" name="operationcode" value="${operationcode }">
			</td>
			<input type="hidden" name="req" value="29">
			<input id="b30" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_pro_drug_operation查询">
				(查询药品开始时间)
			</td>
			<td>
				caseid:<input type="text" name="caseid" value="${caseid }">
			</td>
			<td>
				operationcode:<input type="text" name="operationcode" value="${operationcode }">
			</td>
			<input type="hidden" name="req" value="30">
			<input id="b31" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_user_operation_priv查询">
			</td>
			<td>
				hiscode:<input type="text" name="hiscode" value="${hiscode }">
			</td>
			<td>
				operationcode:<input type="text" name="operationcode" value="${operationcode }">
			</td>
			<input type="hidden" name="req" value="31">
			<input id="b32" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_user_pediatric查询">
			</td>
			<td>
				hiscode:<input type="text" name="hiscode" value="${hiscode }">
			</td>
			<td>
				drug_unique_code:<input type="text" name="drug_unique_code" value="${drug_unique_code }">
			</td>
			<input type="hidden" name="req" value="32">
			<input id="b33" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_user_rendos查询">
			</td>
			<td>
				hiscode:<input type="text" name="hiscode" value="${hiscode }">
			</td>
			<td>
				druguniquecode:<input type="text" name="druguniquecode" value="${druguniquecode }">
			</td>
			<input type="hidden" name="req" value="33">
			<input id="b34" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_user_drugroute查询">
			</td>
			<td>
				hiscode:<input type="text" name="hiscode" value="${hiscode }">
			</td>
			<td>
				druguniquecode:<input type="text" name="druguniquecode" value="${druguniquecode }">
			</td>
			<td>
				routecode:<input type="text" name="routecode" value="${routecode }">
			</td>
			<input type="hidden" name="req" value="34">
			<input id="b35" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_user_sex查询">
			</td>
			<td>
				hiscode:<input type="text" name="hiscode" value="${hiscode }">
			</td>
			<td>
				druguniquecode:<input type="text" name="druguniquecode" value="${druguniquecode }">
			</td>
			<input type="hidden" name="req" value="35">
			<input id="b36" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_user_unage_sp查询">
			</td>
			<td>
				moduleid:<input type="text" name="moduleid" value="${moduleid }">
			</td>
			<td>
				hiscode:<input type="text" name="hiscode" value="${hiscode }">
			</td>
			<td>
				druguniquecode:<input type="text" name="druguniquecode" value="${druguniquecode }">
			</td>
			<input type="hidden" name="req" value="36">
			<input id="b37" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
	</table>
	</c:if>
	
	<c:if test="${tabletype==3 }">
	<table>
		<tr>
			屏蔽表采用模糊查询，搜索和药品有关的所有数据
		</tr>
		<tr>
			<form action="Passredisquery.action" method="post" onsubmit="return get_shuju();">
			<td>
				<input type="submit" value="mc_user_shielddata查询">
			</td>
			<td>
				moduleid:<input type="text" name="moduleid" value="${moduleid }">
			</td>
			<td>
				druguniquecode:<input type="text" name="druguniquecode" value="${druguniquecode }">
			</td>
			<input type="hidden" name="req" value="37">
			<input id="b38" type="hidden" name="servername" value="">
			<input type="hidden" name="tabletype" value="${tabletype }">
			</form>
		</tr>
	</table>
	</c:if>
</div>
<div class="redis2">
	<h2>------------------------------redis查询结果------------------------------------</h2>
	<c:forEach begin="0" items="${list }" var="list1">
		<c:out value="${list1 }"></c:out><br><br>
	</c:forEach>
	<c:out value="${str }"></c:out>
</div>
	
</div>
</body>
</html>