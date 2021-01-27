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
<div class="container-fluid d-flex justify-content-center">
    <form name="review" action="<c:url value="/cafe"/>" method="post" class="w-50 needs-validation"
          novalidate accept-charset="UTF-8">
        <h1 class="mt-4"><fmt:message key="title.review"/></h1>
        <p id="violations" class="text-danger"></p>
        <p id="server_message" class="text-danger"></p>
        <input type="hidden" name="command" value="user-review">
        <div class="mt-4">
            <label for="code" class="form-label"><fmt:message key="label.reviewCode"/></label>
            <input type="text" id="code" name="code" class="form-control"
                   pattern="^[A-Za-z0-9]{10}$" required/>
            <div class="invalid-feedback">
                <fmt:message key="prescription.reviewCode"/>
            </div>
        </div>
        <div class="mt-4">
            <label for="review" class="form-label"><fmt:message key="label.review"/></label>
            <textarea id="review" name="review" class="form-control" required></textarea>
            <div class="invalid-feedback">
                <fmt:message key="prescription.review"/>
            </div>
        </div>
        <div class="form-group mt-4">
            <div class="rate">
                <input type="radio" id="star5" name="rate" value="5"/>
                <label for="star5" title="text">5 stars</label>
                <input type="radio" id="star4" name="rate" value="4"/>
                <label for="star4" title="text">4 stars</label>
                <input type="radio" id="star3" name="rate" value="3"/>
                <label for="star3" title="text">3 stars</label>
                <input type="radio" id="star2" name="rate" value="2"/>
                <label for="star2" title="text">2 stars</label>
                <input type="radio" id="star1" name="rate" value="1"/>
                <label for="star1" title="text">1 star</label>
            </div>
        </div>
        <div class="input-group mt-4">
            <button type="submit" class="btn btn-dark">
                <fmt:message key="button.submit"/>
            </button>
        </div>
    </form>
</div>
<style>
    .rate {
        float: left;
        height: 46px;
        padding: 0;
        margin: 0 0 25px;
    }

    .rate:not(:checked) > input {
        position: absolute;
        top: -9999px;
    }

    .rate:not(:checked) > label {
        float: right;
        width: 1em;
        overflow: hidden;
        white-space: nowrap;
        cursor: pointer;
        font-size: 30px;
        color: #ccc;
    }

    .rate:not(:checked) > label:before {
        content: '★ ';
    }

    .rate > input:checked ~ label {
        color: #ffc700;
    }

    .rate:not(:checked) > label:hover,
    .rate:not(:checked) > label:hover ~ label {
        color: #deb217;
    }

    .rate > input:checked + label:hover,
    .rate > input:checked + label:hover ~ label,
    .rate > input:checked ~ label:hover,
    .rate > input:checked ~ label:hover ~ label,
    .rate > label:hover ~ input:checked ~ label {
        color: #c59b08;
    }
</style>
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
            window.location.href = '<c:url value="/cafe"/>' + "?command=" + redirectCommand
        }
    }
</script>
<c:import url="../parts/footer.jsp"/>
