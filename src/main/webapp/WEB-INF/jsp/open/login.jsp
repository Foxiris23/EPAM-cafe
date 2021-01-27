<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${not empty sessionScope.locale ? sessionScope.locale : 'ru_RU' }"
               scope="session"/>
<fmt:setBundle basename="content"/>
<%@ taglib prefix="app" uri="/WEB-INF/taglib/app.tld" %>
<c:import url="../parts/header.jsp"/>
<div class="container-fluid d-flex justify-content-center mt-4">
    <form name="login" action="<c:url value="/rest"/>" method="post" class="needs-validation w-25" novalidate>
        <h2><fmt:message key="title.auth"/></h2>
        <p class="text-success">
            ${requestScope.verification_message}
            ${param.verification_message}
        </p>
        <p id="server_message" class="text-danger">${requestScope.server_message}</p>
        <p id="violations" class="text-danger">
            <app:violationMessages violationMessages="${requestScope.violation_message}"/>
        </p>
        <input type="hidden" name="command" value="login">
        <div class="form-group mt-4">
            <label for="username" class="form-label"><fmt:message key="label.username"/></label>
            <input type="text" id="username" name="username" class="form-control"
                   placeholder="<fmt:message key="placeholder.username"/>" required/>
            <div class="invalid-feedback">
                <fmt:message key="prescription.username"/>
            </div>
        </div>
        <div class="form-group mt-3">
            <label for="password" class="form-label"><fmt:message key="label.password"/></label>
            <input type="password" id="password" name="password" class="form-control"
                   placeholder="<fmt:message key="placeholder.password"/>" required/>
            <div class="invalid-feedback">
                <fmt:message key="prescription.password"/>
            </div>
        </div>
        <div class="form-group mt-4">
            <button class="btn btn-dark w-100" type="submit"><fmt:message key="button.login"/></button>
            <div class="mt-2">
                <a href="<c:url value="/cafe?command=to-registration"/>"><fmt:message key="link.register"/></a>
            </div>
        </div>
    </form>
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

</div>
<c:import url="../parts/footer.jsp"/>
