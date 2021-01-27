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
<c:set var="user" value="${requestScope.user}"/>
<div class="d-flex justify-content-center">
    <div class="w-50">
        <p class="display-2 text-center"><fmt:message key="title.profile"/></p>
        <div class="card mt-4">
            <h4 class="card-header"><fmt:message key="title.profileData"/></h4>
            <div class="card-body" style="width: 540px;">
                <h5 class="card-title">${user.firstName} ${user.lastName}</h5>
                <p id="balance" class="card-text">
                    <fmt:message key="label.balance"/>: ${user.balance}
                    <a href="#" onclick="topUp()" class="link-dark"><fmt:message key="link.topUp"/></a>
                </p>
                <p class="card-text"><fmt:message key="label.points"/>: ${user.loyaltyPoints}</p>
                <p class="card-text"><fmt:message key="label.username"/>: ${user.username}</p>
                <p class="card-text"><fmt:message key="label.number"/>: ${user.phoneNumber}</p>
                <p class="card-text"><fmt:message key="label.email"/>: ${user.email}</p>
                <button data-bs-toggle="modal" data-bs-target="#modal" class="btn btn-dark mt-2" type="button">
                    <fmt:message key="button.edit"/>
                </button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="modal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel"><fmt:message key="title.edit"/></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form name="edit" action="<c:url value="/cafe"/>" method="post" class="needs-validation"
                  novalidate accept-charset="UTF-8">
                <div class="modal-body">
                    <p id="violations" class="text-danger"></p>
                    <p id="server_message" class="text-danger"></p>
                    <input type="hidden" name="command" value="user-edit-profile">
                    <div class="row mt-3">
                        <div class="col-6">
                            <label for="first-name" class="form-label"><fmt:message key="label.firstName"/></label>
                            <input type="text" id="first-name" name="first_name" class="form-control"
                                   value='${user.firstName}' pattern="^[A-Za-zА-Яа-яЁё']{2,20}?$" required/>
                            <div class="invalid-feedback">
                                <fmt:message key="prescription.firstName"/>
                            </div>
                        </div>
                        <div class="col-6">
                            <label for="last-name" class="form-label"><fmt:message key="label.lastName"/></label>
                            <input type="text" id="last-name" name="last_name" class="form-control"
                                   value='${user.lastName}' pattern="^[A-Za-zА-Яа-яЁё']{2,20}?$" required/>
                            <div class="invalid-feedback">
                                <fmt:message key="prescription.lastName"/>
                            </div>
                        </div>
                    </div>
                    <label for="number" class="form-label mt-4"><fmt:message key="label.number"/></label>
                    <div class="input-group has-validation">
                        <input type="text" id="number" name="number" class="form-control" value='${user.phoneNumber}'
                               pattern="^\+375((44)|(33)|(29))[0-9]{7}$" required/>
                        <div class="invalid-feedback">
                            <fmt:message key="prescription.number"/>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <fmt:message key="button.close"/>
                    </button>
                    <button type="submit" class="btn btn-dark">
                        <fmt:message key="button.save"/>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    async function topUp() {
        let data = new FormData();
        data.append('command', 'user-top-up')
        jQuery.ajax({
            url: '<c:url value="/rest"/>', data: data, cache: false, contentType: false,
            processData: false, method: 'POST',
            success: topUpSuccess
        });

        function topUpSuccess(data) {
            document.location.reload();
            let parse = JSON.parse(data);
            let redirectCommand = parse.redirect_command;
            if (redirectCommand != null) {
                window.location.href = '<c:url value="/cafe"/>' + "?command=" + redirectCommand;
            }
        }
    }

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
            window.location.href = '<c:url value="/cafe"/>' + "?command=" + redirectCommand
        }
    }
</script>
<c:import url="../parts/footer.jsp"/>
