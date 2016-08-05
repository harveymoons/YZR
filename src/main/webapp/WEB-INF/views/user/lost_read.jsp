<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/header.jsp"%>
<%@include file="./sidebar.jsp"%>
<style>
tr{
	height: 60px;
}
td{
	text-align: left;
}
th{
	width: 100px;
	text-align: left;
	font-weight: bold;
}
.table > tbody > tr > th, .table > tbody > tr > td{
	vertical-align: middle;
	background-color: ivory;
}
</style>
<div class="content" style="margin-left: 250px;">
	<div>
		<h3>나의 분실물 문의</h3>
	</div>

	<table class="table">
		<tr style="border: 0px;">
			<th style="border-top: 0px;">분실장소</th>
			<td colspan="3" style="border-top: 0px;">
				${lostvo.lost_place }
			</td>
		</tr>
		<tr>
			<th>분실 날짜</th>
			<td colspan="3">
				${lostvo.lost_date }
			</td>
		</tr>
		<tr>
			<th>분실문 종류</th>
			<td colspan="3">
				${lostvo.lost_type }
			</td>
		</tr>
		<tr>
			<th>내용</th>
			<td colspan="3">
				<textarea style="resize:none;" readonly="readonly" id="lost_content" name="lost_content" rows="5" cols="100" >${lostvo.lost_content }
				</textarea>
			</td>
		</tr>
	</table><br><br>
	<div style="float: right;">
		<a href="/user/lost"><input type="button" class="btn btn-default" value="확인"></a>
	</div>
</div>
<%@include file="../include/footer.jsp"%>