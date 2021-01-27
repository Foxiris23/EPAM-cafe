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
<c:url var="url" value="/cafe?command=to-reviews&page="/>
<p class="display-2 text-center"><fmt:message key="title.reviews"/></p>
<table class="table">
    <thead>
    <tr>
        <th scope="col">#</th>
        <th scope="col"><fmt:message key="label.orderId"/></th>
        <th scope="col"><fmt:message key="label.username"/></th>
        <th scope="col"><fmt:message key="label.rate"/></th>
        <th scope="col"></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${requestScope.pageable.list}" var="review">
        <tr>
            <th scope="row">
                <div class="mt-1">${review.id}</div>
            </th>
            <td>
                <div class="mt-1">${review.order.id}</div>
            </td>
            <td>
                <div class="mt-1">${review.order.user.username}</div>
            </td>
            <td>${review.rate}</td>
            <td>
                <div class="modal fade" id="modal-${review.id}"
                     tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="exampleModalLabel"><fmt:message key="title.feedback"/></h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
                                </button>
                            </div>
                            <div class="modal-body">
                                <p>${review.feedback}</p>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row justify-content-end">
                    <button type="button" class="btn btn-dark" data-bs-toggle="modal"
                            data-bs-target="#modal-${review.id}">
                        <fmt:message key="button.feedback"/>
                    </button>
                </div>
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
<c:import url="../parts/footer.jsp"/>
