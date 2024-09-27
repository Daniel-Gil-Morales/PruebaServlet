<%@ include file="header.jsp" %>

<h1>
    <c:choose>
        <c:when test="${province == null}">
            <fmt:message key="msg.province-form.add" />
        </c:when>
        <c:otherwise>
            <fmt:message key="msg.province-form.edit" />
        </c:otherwise>
    </c:choose>
</h1>

<form action="provinces" method="post">
    <input type="hidden" name="id" value="${province != null ? province.id : ''}" />
    <input type="hidden" name="action" value="${province == null ? 'insert' : 'update'}" />

    <label for="code"><fmt:message key='msg.province-form.code' />:</label>
    <input type="text" name="code" id="code" value="${province != null ? province.code : ''}" required />

    <label for="name"><fmt:message key='msg.province-form.name' />:</label>
    <input type="text" name="name" id="name" value="${province != null ? province.name : ''}" required />

    <label for="id_region"><fmt:message key='msg.province-form.region' />:</label>
    <select name="id_region" id="id_region" required>
        <c:forEach var="region" items="${listRegions}">
            <option value="${region.id}" ${province != null && province.region.id == region.id ? 'selected' : ''}>
                ${region.name}
            </option>
        </c:forEach>
    </select>

    <c:choose>
        <c:when test="${province == null}">
            <input type="submit" value="<fmt:message key='msg.province-form.create' />" />
        </c:when>
        <c:otherwise>
            <input type="submit" value="<fmt:message key='msg.province-form.update' />" />
        </c:otherwise>
    </c:choose>
</form>

<a href="provinces"><fmt:message key="msg.province-form.returnback" /></a>

<%@ include file="footer.jsp" %>
