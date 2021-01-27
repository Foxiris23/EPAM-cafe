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
        <p class="display-3 text-center"><fmt:message key="title.checkout"/></p>
        <p id="server_message" class="text-danger"></p>
        <p id="violations" class="text-danger"></p>
        <form name="checkout" action="<c:url value="/rest"/>" method="post" class="needs-validation" novalidate>
            <input id="checkout-cart" type="text" name="cart" hidden>
            <input type="text" name="command" hidden value="user-checkout">
            <div class="form-group mt-5">
                <label for="address" class="form-label"><fmt:message key="label.deliveryAddress"/></label>
                <input id="address" name="address" type="text" class="form-control"
                       placeholder="<fmt:message key="placeholder.address"/>"
                       pattern="^.{2,50}?$" required/>
                <div class="invalid-feedback">
                    <fmt:message key="prescription.address"/>
                </div>
            </div>
            <div class="row mt-4">
                <div class="col-6">
                    <label for="select" class="form-label"><fmt:message key="label.paymentMethod"/></label>
                    <select id="select" name="method" class="form-select">
                        <option value="BALANCE" selected><fmt:message key="option.balance"/></option>
                        <option value="CASH"><fmt:message key="option.cash"/></option>
                        <option value="CARD"><fmt:message key="option.card"/></option>
                    </select>
                </div>
                <div class="col-6">
                    <label for="time" class="form-label"><fmt:message key="label.dateAndTime"/></label>
                    <input id="time" type="datetime-local" class="form-control" name="time" required>
                </div>
            </div>
            <div class="mt-5">
                <label for="checkout" class="form-label">
                    <fmt:message key="label.totalCost"/> ${requestScope.total_cost}$
                </label>
                <button id="checkout" class="btn btn-dark w-100" onclick="cartToJson(null, 'checkout-cart')"
                        type="submit">
                    <fmt:message key="button.checkout"/>
                </button>
            </div>
        </form>
    </div>
</div>
<script>
    function onAjaxSuccess(data) {
        let pViolations = document.getElementById("violations");
        let pMessages = document.getElementById("server_message");
        pViolations.innerText = "";
        pMessages.innerText = "";
        let parse = JSON.parse(data);
        let violations = parse.violation_message;
        if (violations != null) {
            for (let i = 0; i < violations.length; i++) {
                pViolations.innerText += violations[i] + '\n';
            }
        }
        let serverMessages = parse.server_message;
        if (serverMessages != null) {
            pMessages.innerText += serverMessages + '\n';
        }

        let redirectCommand = parse.redirect_command;
        if (redirectCommand != null) {
            localStorage.clear();
            window.location.href = '<c:url value="/cafe"/>' + "?command=" + redirectCommand;
        }
    }
</script>
<c:import url="../parts/footer.jsp"/>
