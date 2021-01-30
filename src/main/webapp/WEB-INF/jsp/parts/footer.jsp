<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"
        integrity="sha384-q2kxQ16AaE6UbzuKqyBE9/u/KzioAlnx2maXQHiDX9d4/zp8Ok3f+M7DPm+Ib6IU"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.min.js"
        integrity="sha384-pQQkAEnwaBkjpqZ8RU1fF1AKtTcHJwFl3pblpTlHXybJjHpMYo79HY3hIi4NKxyj"
        crossorigin="anonymous"></script>
<script>
    document.getElementById("currUrl").value = window.location.search;

    <c:if test="${isAuthorized}">
    <c:if test="${not empty sessionScope.cart}">
    <c:if test="${sessionScope.cart.size() gt 0}">
    document.getElementById("cart").src = '<c:url value="../../../img/shopping-cart-red.svg"/>';
    </c:if>
    </c:if>

    document.getElementById("logout").addEventListener('click', function () {
        localStorage.clear();
    });

    </c:if>

    (function () {
        'use strict'

        const forms = document.querySelectorAll('.needs-validation')

        Array.prototype.slice.call(forms)
            .forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    event.preventDefault()
                    if (!form.checkValidity()) {
                        event.stopPropagation()
                    } else {
                        sendRequest(form.name)
                    }
                    form.classList.add('was-validated')
                }, false)
            })
    })()

    function sendRequest(name) {
        let form = document.forms[name]
        let data = new FormData(form)

        jQuery.ajax({
            url: '<c:url value="/rest"/>',
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            method: 'POST',
            success: onAjaxSuccess
        });
    }
</script>
</div>
</body>
</html>