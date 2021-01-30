<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="locale"
       value="${not empty sessionScope.locale ? sessionScope.locale : 'ru_RU'}"/>
<fmt:setBundle basename="content"/>
<fmt:setLocale value="${locale}"
               scope="session"/>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="<c:url value="/cafe?command=to-main"/>">
            <img src="<c:url value='/img/logo.png'/>" alt="Epam cafe" width="40" height="40"/>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse mr-5 row" id="navbarSupportedContent">
            <ul class="navbar-nav mb-2 mb-lg-0 col-9">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page"
                       href="<c:url value="/cafe?command=to-main"/>"><fmt:message key="link.home"/></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page"
                       href="<c:url value='/cafe?command=to-menu'/>"><fmt:message key="link.menu"/></a>
                </li>
                <c:if test="${isAuthorized}">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page"
                           href="<c:url value="/cafe?command=user-to-profile"/>">
                            <fmt:message key="link.profile"/></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page"
                           href="<c:url value="/cafe?command=user-to-my-orders"/>">
                            <fmt:message key="link.myOrders"/></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page"
                           href="<c:url value="/cafe?command=user-to-review"/>">
                            <fmt:message key="link.review"/></a>
                    </li>
                    <c:if test="${isAdmin}">
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="<c:url
                            value="/cafe?command=admin-to-users&page=1"/>">
                                <fmt:message key="link.users"/></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="<c:url
                            value="/cafe?command=admin-to-orders&page=1"/>">
                                <fmt:message key="link.orders"/></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="<c:url
                            value="/cafe?command=admin-to-reviews&page=1"/>">
                                <fmt:message key="link.reviews"/></a>
                        </li>
                    </c:if>
                </c:if>
            </ul>
            <div class="navbar-nav col-3 d-flex justify-content-end px-2">
                <c:if test="${isAuthorized}">
                    <a href="<c:url value="/cafe?command=user-to-cart"/>">
                        <img id="cart" class="mx-3" src="<c:url value='../../../img/shopping-cart.svg'/>" alt="cart"
                             width="35" height="35"/>
                    </a>
                </c:if>
                <form action="<c:url value="/cafe"/>" method="post">
                    <input type="hidden" name="command" value="locale-switch">
                    <input id="currUrl" type="hidden" name="current_page" value="">
                    <select class="form-select" name="locale" onchange="submit()">
                        <option value="en_US" <c:if test="${locale eq 'en_US'}">selected</c:if>>English</option>
                        <option value="ru_RU" <c:if test="${locale eq 'ru_RU'}">selected</c:if>>Русский</option>
                        <option value="be_BY" <c:if test="${locale eq 'be_BY'}">selected</c:if>>Беларуская</option>
                    </select>
                </form>
                <c:if test="${not isAuthorized}">
                    <form class="mx-3" action="<c:url value='/cafe'/>">
                        <input type="hidden" name="command" value="to-login">
                        <button class="btn btn-outline-light"><fmt:message key="button.login"/></button>
                    </form>
                </c:if>
                <c:if test="${isAuthorized}">
                    <form class="mx-3" action="<c:url value='/cafe'/>">
                        <input type="hidden" name="command" value="user-logout">
                        <button id="logout" class="btn btn-outline-light"><fmt:message key="button.logout"/></button>
                    </form>
                </c:if>
            </div>
        </div>
    </div>
</nav>