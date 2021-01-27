<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${ not empty sessionScope.locale ? sessionScope.locale : 'ru_RU' }" scope="session"/>
<fmt:setBundle basename="content"/>
<%@ taglib prefix="app" uri="/WEB-INF/taglib/app.tld" %>
<c:import url="../parts/header.jsp"/>
<div class="d-flex justify-content-center mt-0">
    <p class="display-1"><fmt:message key="title.menu"/></p>
</div>
<div class="row justify-content-center">
    <c:forEach items="${requestScope.menu_items}" var="item">
        <div class="card col-6 m-5" style="width: 18rem;">
            <a class="link-dark text-decoration-none"
               href="<c:url value='/cafe?command=to-menu-item&type_id=${item.id}&page=1'/>">
                <img src="<c:url value='../../../uploads/${item.filename}'/>" class="card-img-top"
                     alt="${item.name}">
                <div class="card-body row align-content-end">
                    <p class="text-center">${item.name}</p>
                </div>
            </a>
            <c:if test="${isAdmin}">
                <button type="button" class="btn btn-dark mb-2" data-bs-toggle="modal" data-bs-target="#exampleModal"
                        onclick='prepareModal("${item.id}", "${item.name}")'>
                    <fmt:message key="button.edit"/>
                </button>
            </c:if>
        </div>
    </c:forEach>
    <c:if test="${isAdmin}">
        <div class="card col-6 m-5" style="width: 18rem;">
            <a class="link-dark text-decoration-none" href="<c:url value="/cafe?command=admin-to-add-type"/>">
                <div class="d-flex justify-content-center">
                    <img src="<c:url value="../../../img/add-button.svg"/>" class="card-img-top mt-1"
                         alt="Plus" style="width: 12rem; height: 12rem">
                </div>
                <div class="card-body text-center">
                    <p><fmt:message key="link.addType"/></p>
                </div>
            </a>
        </div>
    </c:if>
</div>
<c:if test="${isAdmin}">
    <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel"><fmt:message key="title.edit"/></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form name="editType" action="<c:url value="/rest"/>" method="post" class="needs-validation"
                      novalidate
                      enctype="multipart/form-data">
                    <div class="modal-body">
                        <p id="violations" class="text-danger"></p>
                        <p id="server_message" class="text-danger"></p>
                        <input type="text" hidden name="command" value="admin-edit-type">
                        <input id="id" type="text" hidden name="id" value="">
                        <div class="form-group mt-4">
                            <label for='type-name' class="form-label"><fmt:message key="label.productTypeName"/></label>
                            <input id="type-name" class="form-control" type="text" name="product-name"
                                   placeholder="<fmt:message key="placeholder.productTypeName"/>"
                                   pattern="^[A-Za-zа-яА-Я\s'-]{4,20}?$"
                                   required>
                            <div class="invalid-feedback">
                                <fmt:message key="prescription.productTypeName"/>
                            </div>
                        </div>
                        <div class="form-group mt-4">
                            <label for="img" class="form-label"><fmt:message key="label.imgFile"/></label>
                            <input id="img" class="form-control" type="file" name="img-0"
                                   accept="image/x-png,image/jpeg"
                                   required>
                        </div>
                    </div>
                </form>
                <div class="modal-footer">
                    <form name="deleteType" action="<c:url value="/rest"/>" method="post" class="needs-validation"
                          novalidate>
                        <input type="text" hidden name="command" value="admin-delete-type" required>
                        <input id="deleteId" type="text" hidden name="id" value="" required>
                        <button type="submit" class="btn btn-danger">
                            <fmt:message key="button.delete"/>
                        </button>
                    </form>
                    <button onclick="triggerForm()" class="btn btn-dark">
                        <fmt:message key="button.save"/>
                    </button>
                </div>
            </div>
        </div>
    </div>
    <script>
        function prepareModal(id, name) {
            document.getElementById('type-name').value = name;
            document.getElementById('id').value = id;
            document.getElementById('deleteId').value = id;
        }

        function triggerForm() {
            const htmlFormElement = document.forms.namedItem('editType');
            if(htmlFormElement.checkValidity()){
                sendRequest(htmlFormElement.name);
            }
            htmlFormElement.classList.add('was-validated');
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
</c:if>
<c:import url="../parts/footer.jsp"/>