<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>

<openmrs:require privilege="Manage Role Restrictions" otherwise="/login.htm" redirect="/module/restrictbyrole/restriction.list" />

<h2>
	<spring:message code="restrictbyrole.title"/>
</h2>

<b class="boxHeader"><spring:message code="restrictbyrole.current"/></b>

<div class="box">
	<c:if test="${fn:length(restrictionList) == 0}">
		<spring:message code="general.none"/>
	</c:if>
	<c:if test="${fn:length(restrictionList) != 0}">
		<form method="post">
			<table>
				<thead>
				<tr>
					<th></th>
					<th></th>
					<th><spring:message code="general.id"/></th>
					<th><spring:message code="restrictbyrole.role"/></th>
					<th><spring:message code="restrictbyrole.search"/></th>
					<th><spring:message code="restrictbyrole.observations"/></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="restriction" items="${restrictionList}">
					<tr>
						<td><input type="checkbox" name="deleteId" value="${restriction.id}" /></td>
						<td><a href="restrictionForm.form?restrictionId=${restriction.id}">
							<img src="<c:url value='/images/edit.gif'/>" border="0"/></a>
						</td>
						<td>${restriction.id}</td>
						<c:if test="${restriction.retired == true }">
							<td><del>${restriction.role.role}</del></td>
							<td><del>
								${restriction.serializedObject.name}
								<small>(${restriction.serializedObject.description})</small>
							</del></td>
							<td class="error"><spring:message code="restrictbyrole.retiredRoleRestriction"/></td>
						</c:if>
						<c:if test="${restriction.retired != true }">
							<td>${restriction.role.role}</td>
							<td>
								${restriction.serializedObject.name}
								<small>(${restriction.serializedObject.description})</small>
							</td>
						</c:if>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<br/>
			<input type="submit" value="<spring:message code="general.delete" />"/>
		</form>
	</c:if>
</div>

<br/>
<a href="restrictionForm.form"><spring:message code="general.add"/></a>

<%@ include file="/WEB-INF/template/footer.jsp" %>