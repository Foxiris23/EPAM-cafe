<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="locale" value="${ not empty sessionScope.locale ? sessionScope.locale : 'ru_RU' }"/>
<fmt:setLocale value="${locale}"
               scope="session"/>
<fmt:setBundle basename="content"/>
<%@ taglib prefix="app" uri="/WEB-INF/taglib/app.tld" %>
<c:import url="../parts/header.jsp"/>
<div class="alert alert-warning mt-4" role="alert">
    <h3 class="alert-heading"><fmt:message key="title.error"/></h3>
    <p><fmt:message key="serverMessage.somethingWentWrong"/></p>
    <hr>
    <p class="mb-0"><fmt:message key="serverMessage.tryAgainLater"/></p>
</div>
<c:import url="../parts/footer.jsp"/>
