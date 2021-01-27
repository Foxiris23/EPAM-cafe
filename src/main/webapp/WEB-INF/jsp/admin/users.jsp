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
<c:url var="url" value="/cafe?command=to-users&page="/>
<p class="display-2 text-center"><fmt:message key="title.users"/></p>
<table class="table">
    <thead>
    <tr>
        <th scope="col">#</th>
        <th scope="col"><fmt:message key="label.name"/></th>
        <th scope="col"><fmt:message key="label.number"/></th>
        <th scope="col"><fmt:message key="label.points"/></th>
        <th scope="col"></th>
        <th scope="col"></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${requestScope.pageable.list}" var="user">
        <tr>
            <th scope="row"><div class="mt-1">${user.id}</div></th>
            <td><div class="mt-1">${user.firstName} ${user.lastName}</div></td>
            <td><div class="mt-1">${user.phoneNumber}</div></td>
            <td>
                <input name="points" class="form-control" type="text" id="points-${user.id}"
                       value="${user.loyaltyPoints}" style="max-width: 9rem;">
            </td>
            <td>
                <div class="form-check form-switch mt-1">
                    <label class="form-check-label" for="check-${user.id}"> <fmt:message key="label.isBanned"/></label>
                    <input name="check" class="form-check-input" type="checkbox" id="check-${user.id}"
                           <c:if test="${user.isBlocked}">checked</c:if>>
                </div>
            </td>
            <td>
                <button onclick="save(${user.id})" class="btn btn-dark"><fmt:message key="button.save"/></button>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="d-flex justify-content-center m-4">
    <ul class="pagination">
        <app:pagination pages="${requestScope.pageable.totalPages}" page="${requestScope.pageable.page}" url='${url}'/>
    </ul>
</div>
<script>
    async function save(id) {
        let data = new FormData();
        data.append('id', id);
        data.append('points', document.getElementById('points-' + id).value);
        data.append('check', document.getElementById('check-' + id).checked);
        data.append('command', 'admin-update-user');
        jQuery.ajax({
            url: '<c:url value="/rest"/>', data: data, cache: false, contentType: false,
            processData: false, method: 'POST',
            success: successSave
        });

        function successSave(data) {
            let parse = JSON.parse(data);
            let redirectCommand = parse.redirect_command;
            if (redirectCommand != null) {
                window.location.href = '<c:url value="/cafe"/>' + "?command=" + redirectCommand;
            }
            alert('<fmt:message key="alert.saveSuccess"/>')
        }
    }
</script>
<c:import url="../parts/footer.jsp"/>
