<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="isAuthorized" value="${not empty sessionScope.user}" scope="request"/>
<c:set var="userSec" value="${sessionScope.user}" scope="request"/>
<c:set var="username" value="${userSec.username}" scope="request"/>
<c:set var="userId" value="${userSec.id}" scope="request"/>
<c:set var="isAdmin" value="${userSec.role.name eq 'ADMIN'}" scope="request"/>