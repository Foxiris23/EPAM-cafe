<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${ not empty sessionScope.locale ? sessionScope.locale : pageContext.request.locale }"
               scope="session"/>
<fmt:setBundle basename="content"/>
<%@ taglib prefix="app" uri="/WEB-INF/taglib/app.tld" %>
<c:import url="../parts/header.jsp"/>
<div class="container-fluid d-flex justify-content-center mt-4">
    <form name="register" action="<c:url value="/cafe"/>" method="post" class="needs-validation w-50" novalidate
          accept-charset="UTF-8">
        <h2><fmt:message key="title.registration"/></h2>
        <p id="server-message" class="text-danger"></p>
        <p id="violations" class="text-danger"></p>
        <input type="hidden" name="command" value="registration"/>
        <div class="row mt-3">
            <div class="col-6">
                <label for="first-name" class="form-label"><fmt:message key="label.firstName"/></label>
                <input type="text" id="first-name" name="first_name" class="form-control"
                       placeholder="<fmt:message key="placeholder.firstName"/>" pattern="^[A-Za-zА-Яа-яЁё']{2,20}?$"
                       required/>
                <div class="invalid-feedback">
                    <fmt:message key="prescription.firstName"/>
                </div>
            </div>
            <div class="col-6">
                <label for="last-name" class="form-label"><fmt:message key="label.lastName"/></label>
                <input type="text" id="last-name" name="last_name" class="form-control"
                       placeholder="<fmt:message key="placeholder.lastName"/>" pattern="^[A-Za-zА-Яа-яЁё']{2,20}?$"
                       required/>
                <div class="invalid-feedback">
                    <fmt:message key="prescription.lastName"/>
                </div>
            </div>
        </div>
        <div class="form-group mt-4">
            <label for="username" class="form-label"><fmt:message key="label.username"/></label>
            <input type="text" id="username" name="username" pattern="^[A-Za-z0-9_]{5,20}" class="form-control"
                   placeholder="<fmt:message key="placeholder.username"/>" required/>
            <div class="invalid-feedback">
                <fmt:message key="prescription.registerUsername"/>
            </div>
        </div>
        <div class="row mt-3">
            <div class="col-6">
                <div class="form-group mt-3">
                    <label for="password" class="form-label"><fmt:message key="label.password"/></label>
                    <input type="password" id="password" name="password" class="form-control"
                           placeholder="<fmt:message key="placeholder.password"/>"
                           pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}" required/>
                    <div class="invalid-feedback">
                        <fmt:message key="prescription.registerPassword"/>
                    </div>
                </div>
            </div>
            <div class="col-6">
                <div class="form-group mt-3">
                    <label for="passwordRepeat" class="form-label"><fmt:message key="label.repeatPassword"/></label>
                    <input type="password" id="passwordRepeat" name="password_repeat" class="form-control"
                           placeholder="<fmt:message key="placeholder.repeatPassword"/>"
                           pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}" required/>
                    <div class="invalid-feedback">
                        <fmt:message key="prescription.registerPassword"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="row mt-3">
            <div class="col-6">
                <label for="email" class="form-label"><fmt:message key="label.email"/></label>
                <input type="email" id="email" name="email" class="form-control"
                       placeholder="<fmt:message key="placeholder.email"/>" required
                       pattern="^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"/>
                <div class="invalid-feedback">
                    <fmt:message key="prescription.email"/>
                </div>
            </div>
            <div class="col-6">
                <label for="number" class="form-label"><fmt:message key="label.number"/></label>
                <div class="input-group has-validation">
                    <input type="text" id="number" name="number" class="form-control"
                           pattern="^\+375((44)|(33)|(29))[0-9]{7}$" value="+375" required/>
                    <div class="invalid-feedback">
                        <fmt:message key="prescription.number"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group mt-5">
            <button class="btn btn-dark w-100" type="submit"><fmt:message key="button.register"/></button>
            <div class="mt-2">
                <a href="<c:url value="/cafe?command=to-login"/>"><fmt:message key="link.login"/></a>
            </div>
        </div>
    </form>
    <script>
        function onAjaxSuccess(data) {
            let pViolations = document.getElementById("violations");
            let pMessages = document.getElementById("server-message");
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
            let verificationMessage = parse.verification_message
            if (redirectCommand != null) {
                window.location.href = '<c:url value="/cafe"/>'
                    + "?command=" + redirectCommand
                    + '&verification_message='
                    + verificationMessage;
            }
        }
    </script>
</div>
<c:import url="../parts/footer.jsp"/>
