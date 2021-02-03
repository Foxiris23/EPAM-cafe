<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${ not empty sessionScope.locale ? sessionScope.locale : 'ru_RU' }" scope="session"/>
<fmt:setBundle basename="content"/>
<%@ taglib prefix="app" uri="/WEB-INF/taglib/app.tld" %>
<c:import url="../parts/header.jsp"/>
<c:url var="url" value="/cafe?command=to-menu-item&type_id=${requestScope.product_type.id}&page="/>
<div class="d-flex justify-content-center">
    <p class="display-1">${requestScope.product_type.name}</p>
</div>
<div class="row justify-content-center">
    <c:forEach items="${requestScope.pageable.list}" var="item">
        <div class="card text-center m-5 col-6" style="width: 18rem;">
            <img src="<c:url value='../../../uploads/${item.imgFilename}'/>" class="card-img-top"
                 style="max-height: 18rem;"
                 alt="Item">
            <div class="card-body row align-items-end">
                <h5 class="card-title">${item.name}</h5>
                <p class="card-text">${item.description}</p>
                <p class="card-text">${item.price} $</p>
                <c:if test="${isAuthorized}">
                    <form name="addToCart-${item.id}" method="post" action="<c:url value="/rest"/>"
                          class="needs-validation w-100 p-0 m-0" novalidate>
                        <input type="hidden" name="id" value="${item.id}">
                        <input type="hidden" name="command" value="user-add-to-cart">
                        <button type="submit" onclick="added(this)" class="btn btn-dark w-100">
                            <fmt:message key="button.add"/>
                        </button>
                    </form>
                </c:if>
                <c:if test="${isAdmin}">
                    <button type="button" class="btn btn-primary w-100 mt-3" data-bs-toggle="modal"
                            data-bs-target="#exampleModal"
                            onclick='prepareModal("${item.id}", "${item.name}", "${item.price}", "${item.description}")'>
                        <fmt:message key="button.edit"/>
                    </button>
                </c:if>
            </div>
        </div>
    </c:forEach>
    <c:if test="${isAdmin}">
        <div class="card text-center m-5 col-6" style="width: 18rem;">
            <img src="<c:url value="../../../img/add-button.svg"/>" class="card-img-top mt-1" style="max-height: 18rem;"
                 alt="Item">
            <div class="card-body row align-items-end">
                <a href="<c:url value='/cafe?command=admin-to-add-product&type_id=${requestScope.product_type.id}'/>"
                   class="btn btn-dark w-100"><fmt:message key="button.addProduct"/></a>
            </div>
        </div>

        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel"><fmt:message key="title.edit"/></h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form name="editProduct" action="<c:url value="/rest"/>" method="post"
                              class="needs-validation" novalidate
                              enctype="multipart/form-data">
                            <p id="violations" class="text-danger"></p>
                            <p id="server_message" class="text-danger"></p>
                            <input type="hidden" name="command" value='admin-edit-product'>
                            <input id="id" type="hidden" name="id" value="">
                            <div class="row mt-2">
                                <div class="form-group col-6">
                                    <label for='product-name' class="form-label"><fmt:message
                                            key="label.productName"/></label>
                                    <input id="product-name" class="form-control" type="text" name="product-name"
                                           placeholder="<fmt:message key="placeholder.productName"/>"
                                           pattern="^[A-Za-zа-яА-Я\s'-]{4,20}?$"
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
                                <label for='description' class="form-label"><fmt:message
                                        key="label.description"/></label>
                                <input id="description" class="form-control" type="text" name="description"
                                       placeholder="<fmt:message key="placeholder.description"/>" pattern="^.{5,40}$"
                                       required>
                                <div class="invalid-feedback">
                                    <fmt:message key="prescription.description"/>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <form name="deleteType" action="<c:url value="/rest"/>" method="post" class="needs-validation"
                              novalidate>
                            <input type="text" hidden name="command" value="admin-delete-product" required>
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
            function prepareModal(id, name, description, price) {
                document.getElementById('product-name').value = name;
                document.getElementById('id').value = id;
                document.getElementById('deleteId').value = id;
                document.getElementById('description').value = description;
                document.getElementById('price').value = price;
            }

            function triggerForm() {
                const htmlFormElement = document.forms.namedItem('editProduct');
                if (htmlFormElement.checkValidity()) {
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
                        + '&type_id=' + '${param.type_id}' + '&page=' + '${param.page}'
                }

            }
        </script>
    </c:if>
</div>
<div class="d-flex justify-content-center m-4">
    <ul class="pagination">
        <app:pagination pages="${requestScope.pageable.totalPages}" page="${requestScope.pageable.page}" url='${url}'/>
    </ul>
</div>
<c:if test="${isAuthorized}">
    <script>
        function added(btn) {
            btn.classList.remove("btn-dark");
            btn.classList.add("btn-success");
            btn.innerText = '<fmt:message key="button.added"/>';
            document.getElementById("cart").src = '<c:url value="../../../img/shopping-cart-red.svg"/>';
            setTimeout(back, 2000);

            function back() {
                btn.classList.remove("btn-success");
                btn.classList.add("btn-dark");
                btn.innerText = '<fmt:message key="button.add"/>';
            }
        }

        <c:if test="${not isAdmin}">

        function onAjaxSuccess(data) {
            let parse = JSON.parse(data);
            let redirectCommand = parse.redirect_command;
            if (redirectCommand != null) {
                localStorage.clear();
                window.location.href = '<c:url value="/cafe"/>' + "?command=" + redirectCommand;
            }
        }

        </c:if>
    </script>
</c:if>
<c:import url="../parts/footer.jsp"/>