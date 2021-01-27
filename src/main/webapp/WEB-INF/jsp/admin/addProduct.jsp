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
    <form name="addProduct" action="<c:url value="/rest"/>" method="post" class="needs-validation w-50" novalidate
          enctype="multipart/form-data">
        <p class="display-6"><fmt:message key="title.addProduct"/></p>
        <p id="violations" class="text-danger"></p>
        <p id="server_message" class="text-danger"></p>
        <input type="hidden" name="type_id" value='${param.type_id}'>
        <input type="hidden" name="command" value='admin-add-product'>
        <div class="row mt-4">
            <div class="form-group col-6">
                <label for='product-name' class="form-label"><fmt:message key="label.productName"/></label>
                <input id="product-name" class="form-control" type="text" name="product-name"
                       placeholder="<fmt:message key="placeholder.productName"/>" pattern="^[A-Za-zа-яА-Я\s'-]{4,20}?$"
                       required>
                <div class="invalid-feedback">
                    <fmt:message key="prescription.productName"/>
                </div>
            </div>
            <div class="form-group col-6">
                <label for='price' class="form-label"><fmt:message key="label.price"/></label>
                <input id="price" class="form-control" type="text" name="price"
                       pattern="^([0-9]{1,3}\.[0-9]{1,2}|[0-9]{1,2})$"
                       placeholder="<fmt:message key="placeholder.price"/>"
                       required>
                <div class="invalid-feedback">
                    <fmt:message key="prescription.price"/>
                </div>
            </div>
        </div>
        <div class="form-group mt-4">
            <label for='description' class="form-label"><fmt:message key="label.description"/></label>
            <input id="description" class="form-control" type="text" name="description"
                   placeholder="<fmt:message key="placeholder.description"/>" pattern="^.{5,40}$"
                   required>
            <div class="invalid-feedback">
                <fmt:message key="prescription.description"/>
            </div>
        </div>
        <div class="form-group mt-4">
            <label for="img" class="form-label"><ftm:message key="label.imgFileProduct"/></label>
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
            window.location.href = '<c:url value="/cafe"/>' + "?command="
                + redirectCommand + '&type_id=' + parse.type_id + '&page=0';
        }
    }
</script>
<c:import url="../parts/footer.jsp"/>
