<jsp:useBean id="matchDto" scope="request"
             type="com.github.ar4ik4ik.tennisscoreboard.model.dto.OngoingMatchResponseDto"/>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-j1CDi7MgGQ12Z7Qab0qlWQ/Qqz24Gc6BM0thvEMVjHnfYGF0rmFCozFSxQBxwHKO"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}../../static/css/styles.css">
    <title>Match Playing</title>
</head>
<body>
<header class="title-header container">
    <ul class="nav d-flex align-items-center w-100">
        <li class="nav-item">
            <p class="title-header__text">TennisScoreboard</p>
        </li>
        <li class="nav-item ms-auto">
            <ul class="d-flex gap-3 mb-0 p-0">
                <li><a class="nav-link" href="../../index.html">Home</a></li>
                <li><a class="nav-link" href="${req.getContextPath()}/matches">Match List</a></li>
            </ul>
        </li>
    </ul>
</header>
<hr>
<section class="container match-score text-center">
    <h1 class="text-uppercase">Матч завершен !</h1>
    <h2>Победитель:
        <c:choose>
            <c:when test="${matchDto.player1Sets() > matchDto.player2Sets()}">
                ${matchDto.player1().name()}
            </c:when>
            <c:otherwise>
                ${matchDto.player2().name()}
            </c:otherwise>
        </c:choose>
    </h2>
    <div class="table-wrapper rounded-4 overflow-hidden shadow-sm">
        <table class="table table-borderless table m-0 text-center">
            <thead class="result">
            <tr>
                <th class="table-text">Player</th>
                <th class="table-text">Sets</th>
            </tr>
            </thead>
            <tbody>
            <tr class="player1">
                <td class="table-text">${matchDto.player1().name()}</td>
                <td class="table-text">${matchDto.player1Sets()}</td>
            </tr>
            <tr class="player2">
                <td class="table-text">${matchDto.player2().name()}</td>
                <td class="table-text">${matchDto.player2Sets()}</td>
            </tr>
            </tbody>
        </table>
    </div>
</section>
<footer>
    <p>© Tennis Scoreboard, project from <a href="https://github.com/Ar4ik4ik">https://github.com/Ar4ik4ik</a></p>
</footer>
</body>
</html>