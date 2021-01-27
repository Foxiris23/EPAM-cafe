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
<div class="container-fluid d-flex justify-content-center mt-4">
    <div class="container-fluid w-50">
        <c:if test="${requestScope.cart.size() < 1}">
            <p class="text-center display-1 mb-4"><fmt:message key="title.emptyCart"/></p>
        </c:if>
        <c:if test="${requestScope.cart.size() > 0}">
            <p class="text-center display-1 mb-4"><fmt:message key="title.cart"/></p>
        </c:if>
        <c:forEach items="${requestScope.cart}" var="item">
            <div class="card mb-3" style="width: 560px;">
                <div class="row g-0">
                    <div class="col-md-4">
                        <img src="<c:url value="/uploads/${item.getKey().imgFilename}"/>"
                             style="max-width: 12rem; max-height: 12rem;"
                             alt="...">
                    </div>
                    <div class="col-md-8">
                        <div class="card-body">
                            <h5 class="card-title text-center">${item.getKey().name}</h5>
                            <p class="card-text text-center">${item.getKey().description}</p>
                            <p class="card-text text-center">${item.getKey().price}$</p>
                            <div class="d-flex justify-content-center">
                                <div class="row align-items-end">
                                    <div class="btn-group" role="group">
                                        <button type="button" onclick="add(${item.getKey().id}, -1)"
                                                class="btn btn-danger" style="width: 2rem;">-
                                        </button>
                                        <button id="${item.getKey().id}" type="button" class="btn btn-outline-dark"
                                                style="min-width: 2rem; border-width: 0" disabled>${item.getValue()}
                                        </button>
                                        <button type="button" onclick="add(${item.getKey().id}, 1)"
                                                class="btn btn-success" style="width: 2rem;">+
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
        <c:if test="${requestScope.cart.size() > 0}">
            <form class="mt-5" action="<c:url value="/cafe"/>" method="post" style="max-width: 500px;">
                <input type="text" hidden name="command" value="user-to-create-order">
                <input id="inputCart" type="text" name="cart" hidden>
                <button class="btn btn-dark" type="button" onclick="cartToJson(this.parentElement, 'inputCart')"
                        style="width: 560px;">
                    <fmt:message key="button.checkout"/>
                </button>
            </form>
        </c:if>
    </div>
</div>
<script>
    <c:if test="${requestScope.cart.size() > 0}">

    async function add(id, number) {
        let amount = localStorage.getItem(id);
        if (amount == null) {
            if (parseInt(number) > 0) {
                localStorage.setItem(id, '1');
                document.getElementById(id).innerText = '1';
            }
        } else {
            let newAmount = parseInt(amount) + parseInt(number);
            if (newAmount < 1) {
                localStorage.removeItem(id);
                document.getElementById(id).innerText = '0';
            } else {
                localStorage.setItem(id, newAmount + '');
                document.getElementById(id).innerText = newAmount + '';
            }
        }
    }

    </c:if>
</script>
<c:import url="../parts/footer.jsp"/>