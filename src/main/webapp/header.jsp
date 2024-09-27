<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" />


<!DOCTYPE html>
<html lang="${sessionScope.locale.language}">
<head>
   <!-- Esta directiva debe ir dentro del head si se usan mensajes en el <title> -->
   <fmt:setBundle basename="messages"/>
   <title><fmt:message key="msg.title" /></title>
</head>
<body>
<!-- Esta directiva debe ir dentro del body para que tenga efecto sobre todos los mensajes del body -->
<fmt:setBundle basename="messages"/>
<form action="changeLanguage" method="get">
   <select name="lang" onchange="this.form.submit()">
       <option value="en" ${sessionScope.locale.language == 'en' ? 'selected' : ''}>English</option>
       <option value="es" ${sessionScope.locale.language == 'es' ? 'selected' : ''}>Español</option>
   </select>
</form>