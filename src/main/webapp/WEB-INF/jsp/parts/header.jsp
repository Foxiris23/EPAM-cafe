<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <title>Cafe</title>
    <link rel="shortcut icon" href="<c:url value="/img/favicon.png"/>" type="image/png">
</head>
<body>
<c:import url="/WEB-INF/jsp/parts/security.jsp"/>
<c:import url="/WEB-INF/jsp/parts/navbar.jsp"/>
<div <c:if test="${empty param.noWrapper}">class="container-md p-5 mt-1"</c:if>>
