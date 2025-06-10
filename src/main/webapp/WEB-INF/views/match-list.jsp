<jsp:useBean id="totalPages" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="currentPage" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="maxItems" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="playerNameFilter" scope="request" type="java.lang.String"/>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-j1CDi7MgGQ12Z7Qab0qlWQ/Qqz24Gc6BM0thvEMVjHnfYGF0rmFCozFSxQBxwHKO"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="./../../static/css/styles.css">
    <title>Match List</title>
</head>
<body>
<header class="title-header container c">
    <ul class="nav d-flex align-items-center w-100">
        <li class="nav-item">
            <p class="title-header__text">TennisScoreboard</p>
        </li>
        <li class="nav-item ms-auto">
            <ul class="d-flex gap-3 mb-0 p-0">
                <li><a class="nav-link" href="../../index.html">Home</a></li>
                <li><a class="nav-link" href="${req.getContextPath()}/matches}">Match List</a></li>
            </ul>
        </li>
    </ul>
</header>
<hr>
<section class="container">
    <div class="row justify-content-center align-items-center">
        <div class="col-12">
            <h1 class="text-uppercase text-center mb-4">Список завершенных матчей</h1>
            <!-- Поисковая форма -->
            <form class="input-group mb-4" action="${pageContext.request.contextPath}/matches">
                <label for="filter_by_player_name" class="visually-hidden">Поиск по имени игрока</label>
                <input
                        type="text"
                        class="input-form form-control form-control-lg rounded-4"
                        id="filter_by_player_name"
                        placeholder="Имя игрока"
                        name="filter_by_player_name"
                        aria-label="Поиск по имени"
                        aria-describedby="search-submit"
                >
                <button
                        class="btn btn-primary btn-sm"
                        type="submit"
                        id="search-submit"
                >
                    Найти
                </button>
            </form>
            <div class="table-wrapper rounded-4 overflow-hidden shadow-sm">
                <table class="table table-borderless table m-0">
                    <thead class="result">
                    <tr>
                        <th class="table-text">First Player</th>
                        <th class="table-text">Second Player</th>
                        <th class="table-text">Winner</th>
                    </tr>
                    </thead>
                    <tbody>
                    <jsp:useBean id="matchesList" scope="request" type="java.util.List"/>
                    <c:forEach items="${matchesList}" var="match">
                        <tr>
                            <td class="table-text">${match.firstPlayerName()}</td>
                            <td class="table-text">${match.secondPlayerName()}</td>
                            <td class="table-text text-success-emphasis fw-bold">${match.winnerName()}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</section>
<section class="fixed-bottom" style="position: fixed; bottom: 50px; left: 0; right: 0; z-index: 1000;">
    <c:if test="${totalPages > 0}">

        <nav class="d-flex justify-content-center align-items-center" aria-label="Навигация по страницам">
            <ul class="pagination">
                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                    <a class="page-link" href="?page=${currentPage - 1}&maxItems=${maxItems}${(playerNameFilter != null and !playerNameFilter.isBlank()) ? '&filter_by_player_name=' += playerNameFilter : '' }" aria-label="Предыдущая">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>

                <c:set var="startPage" value="${(currentPage > 1) ? currentPage - 1 : 1}" />
                <c:set var="endPage" value="${(currentPage + 2 <= totalPages) ? currentPage + 2 : totalPages}" />

                <c:forEach begin="${startPage}" end="${endPage}" var="i">
                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                        <a class="page-link" href="?page=${i}&maxItems=${maxItems}${(playerNameFilter != null and !playerNameFilter.isBlank()) ? '&filter_by_player_name=' += playerNameFilter : '' }">${i}</a>
                    </li>
                </c:forEach>

                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                    <a class="page-link" href="?page=${currentPage + 1}&maxItems=${maxItems}${(playerNameFilter != null and !playerNameFilter.isBlank()) ? '&filter_by_player_name=' += playerNameFilter : '' }" aria-label="Следующая">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>
    </c:if>
</section>
<footer style="position: fixed; bottom: 0; left: 0; right: 0; z-index: 1001;">
    <p>© Tennis Scoreboard, project from <a href="https://github.com/Ar4ik4ik">https://github.com/Ar4ik4ik</a></p>
</footer>
</body>
</html>
