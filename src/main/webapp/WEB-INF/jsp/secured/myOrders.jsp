<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="locale" value="${ not empty sessionScope.locale ? sessionScope.locale : 'ru_RU' }"/>
<fmt:setLocale value="${locale}"
               scope="session"/>
<fmt:setBundle basename="content"/>
<%@ taglib prefix="app" uri="/WEB-INF/taglib/app.tld" %>
<%@ taglib prefix="ftm" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="../parts/header.jsp"/>
<div class="d-flex justify-content-center">
    <p class="display-1"><fmt:message key="title.myOrders"/></p>
</div>
<div class="row justify-content-center mb-4">
    <c:forEach items="${requestScope.orders}" var="order">
        <div class="card m-4 col-6" style="width: 18rem;">
            <div class="card-body">
                <c:if test="${order.status.name() eq 'ACTIVE'}">
                    <h5 class="card-title text-success"><fmt:message key="title.activeOrder"/></h5>
                </c:if>
                <c:if test="${order.status.name() ne 'ACTIVE'}">
                    <h5 class="card-title"><fmt:message key="title.notActiveOrder"/></h5>
                </c:if>
                <h6 class="card-subtitle mb-2 text-muted">${order.cost} $</h6>
                <c:forEach items="${order.products.entrySet()}" var="product">
                    <p class="card-text">${product.getKey().name} X${product.getValue()}</p>
                </c:forEach>
                <hr>
                <p class="card-text"><fmt:message key="label.deliveryDate"/>:</p>
                <input class="form-control" type="datetime-local" readonly value="${order.deliveryDate}">
            </div>
        </div>
    </c:forEach>
</div>
<c:import url="../parts/footer.jsp"/>
