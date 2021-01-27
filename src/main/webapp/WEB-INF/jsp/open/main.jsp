<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${ not empty sessionScope.locale ? sessionScope.locale : pageContext.request.locale }"
               scope="session"/>
<fmt:setBundle basename="content"/>
<c:import url="../parts/header.jsp">
    <c:param name="noWrapper" value="${true}"/>
</c:import>
<div id="carouselExampleCaptions" class="carousel slide" data-bs-ride="carousel">
    <ol class="carousel-indicators">
        <li data-bs-target="#carouselExampleCaptions" data-bs-slide-to="0" class="active"></li>
        <li data-bs-target="#carouselExampleCaptions" data-bs-slide-to="1"></li>
        <li data-bs-target="#carouselExampleCaptions" data-bs-slide-to="2"></li>
    </ol>
    <div class="carousel-inner">
        <div class="carousel-item active">
            <img src="<c:url value="/img/coffee-main-bg.jpg"/>" class="d-block w-100" alt="main">
            <div class="carousel-caption d-none d-md-block">
                <div class="d-flex justify-content-center row">
                    <div class="col-4" style="backdrop-filter: blur(2px); border-radius: 15px;">
                        <h2><fmt:message key="title.main"/></h2>
                        <p style="font-size: 16px;"><fmt:message key="label.main.cafe"/></p>
                    </div>
                </div>
            </div>
        </div>
        <div class="carousel-item">
            <img src="<c:url value="/img/burger-main-bg.jpg"/>" class="d-block w-100" alt="burger">
            <div class="carousel-caption d-none d-md-block">
                <div class="d-flex justify-content-center row">
                    <div class="col-5" style="backdrop-filter: blur(2px); border-radius: 15px;">
                        <h2><fmt:message key="title.main.burgers"/></h2>
                        <p style="font-size: 16px;"><fmt:message key="label.main.burgers"/></p>
                    </div>
                </div>
            </div>
        </div>
        <div class="carousel-item">
            <img src="<c:url value="/img/delivery-main-bg.jpg"/>" class="d-block w-100" alt="delivery">
            <div class="carousel-caption d-none d-md-block text-dark">
                <div class="d-flex justify-content-center row">
                    <div class="col-4" style="backdrop-filter: blur(2px); border-radius: 15px;">
                        <h2><fmt:message key="title.main.delivery"/></h2>
                        <p style="font-size: 16px;"><fmt:message key="label.main.delivery"/></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <a class="carousel-control-prev" href="#carouselExampleCaptions" role="button" data-bs-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Previous</span>
    </a>
    <a class="carousel-control-next" href="#carouselExampleCaptions" role="button" data-bs-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Next</span>
    </a>
</div>
<c:import url="../parts/footer.jsp"/>