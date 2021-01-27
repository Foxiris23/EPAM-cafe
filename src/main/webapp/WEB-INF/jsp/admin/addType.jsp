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
    <form name="addType" action="<c:url value="/rest"/>" method="post" class="needs-validation w-50" novalidate
          enctype="multipart/form-data">
        <p class="display-6"><fmt:message key="title.addProductType"/></p>
        <p id="violations" class="text-danger"></p>
        <p id="server_message" class="text-danger"></p>
        <input type="text" hidden name="command" value="admin-add-type">
        <div class="form-group mt-4">
            <label for='type-name' class="form-label"><fmt:message key="label.productTypeName"/></label>
            <input id="type-name" class="form-control" type="text" name="product-name"
                   placeholder="<fmt:message key="placeholder.productTypeName"/>" pattern="^[A-Za-zа-яА-Я\s'-]{4,20}?$"
                   required>
            <div class="invalid-feedback">
                <fmt:message key="prescription.productTypeName"/>
            </div>
        </div>
        <div class="form-group mt-4">
            <label for="img" class="form-label"><fmt:message key="label.imgFile"/></label>
            <input id="img" class="form-control" type="file" name="img-0" accept="image/x-png,image/jpeg"
                   required>
        </div>
        <button type="submit" class="btn btn-dark mt-5 w-100"><fmt:message key="button.create"/></button>
    </form>
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
            window.location.href = '<c:url value="/cafe"/>' + "?command=" + redirectCommand;
        }
    }
</script>
<c:import url="../parts/footer.jsp"/>