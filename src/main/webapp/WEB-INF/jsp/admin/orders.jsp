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
<c:url var="url" value="/cafe?command=to-orders&page="/>
<p class="display-2 text-center"><fmt:message key="title.orders"/></p>
<table class="table">
    <thead>
    <tr>
        <th scope="col">#</th>
        <th scope="col"><fmt:message key="label.address"/></th>
        <th scope="col"><fmt:message key="label.name"/></th>
        <th scope="col"><fmt:message key="label.number"/></th>
        <th scope="col"><fmt:message key="label.cost"/></th>
        <th scope="col"><fmt:message key="label.status"/></th>
        <th scope="col"></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${requestScope.pageable.list}" var="order">
        <tr>
            <th class="mt-1" scope="row"><div class="mt-1">${order.id}</div></th>
            <td class="mt-1"><div class="mt-1">${order.address}</div></td>
            <td class="mt-1"><div class="mt-1">${order.user.firstName} ${order.user.lastName}</div></td>
            <td class="mt-1"><div class="mt-1">${order.user.phoneNumber}</div></td>
            <td class="mt-1"><div class="mt-1">${order.cost}$</div></td>
            <td><select id="select-${order.id}" class="form-select" aria-label="Default select example" name="select">
                <option value="ACTIVE" <c:if test="${order.status.name() eq 'ACTIVE'}">selected</c:if>>
                    <fmt:message key="label.active"/>
                </option>
                <option value="CANCELLED" <c:if test="${order.status.name() eq 'CANCELLED'}">selected</c:if>>
                    <fmt:message key="label.cancelled"/>
                </option>
                <option value="COMPLETED" <c:if test="${order.status.name() eq 'COMPLETED'}">selected</c:if>>
                    <fmt:message key="label.completed"/>
                </option>
                <option value="UNACCEPTED" <c:if test="${order.status.name() eq 'UNACCEPTED'}">selected</c:if>>
                    <fmt:message key="label.unaccepted"/>
                </option>
            </select></td>
            <td>
                <button onclick="save(${order.id})" class="btn btn-dark"><fmt:message key="button.save"/></button>
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
        data.append('select', document.getElementById('select-' + id).value);
        data.append('command', 'admin-update-order');
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
