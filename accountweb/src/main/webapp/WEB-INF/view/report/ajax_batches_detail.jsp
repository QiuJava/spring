<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<c:if test="${batchesDetailList != null}">
<c:forEach items="${batchesDetailList }" var="item">
	<tr>
		<td>${item.id }</td>
		<td>${item.stepId }</td>
		<td>${item.stepName }</td>
		<td>${item.status }</td>
		<td>${item.executeTime}</td>
		<td>
		<c:if test="${item.status != 0}">
			<a href="javascript:void(0)" onclick="viewLog(${item.id})" >点击查看</a>
		</c:if>
		</td>
		<td>
			<c:if test="${item.status==0 || item.status==2}">
			<a href="javascript:void(0)" onclick="manualSetSuccess(${item.id })">人工处理成功</a>
			<a href="javascript:void(0)" onclick="manualExec(${item.id },${item.stepId})">运行</a>
			</c:if>
		</td>
	</tr>
</c:forEach>
</c:if>