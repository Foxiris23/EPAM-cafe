<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="isAuthorized" value="${not empty sessionScope.user}" scope="request"/>
<c:set var="user" value="${sessionScope.user}" scope="request"/>
<c:set var="username" value="${user.username}" scope="request"/>
<c:set var="userId" value="${user.id}" scope="request"/>
<c:set var="isAdmin" value="${user.role.name eq 'ADMIN'}" scope="request"/>