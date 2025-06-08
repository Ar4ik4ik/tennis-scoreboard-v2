<jsp:useBean id="matchDto" scope="request"
             type="com.github.ar4ik4ik.tennisscoreboard.model.dto.OngoingMatchResponseDto"/>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-j1CDi7MgGQ12Z7Qab0qlWQ/Qqz24Gc6BM0thvEMVjHnfYGF0rmFCozFSxQBxwHKO"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="../../static/css/styles.css">
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
<section class="container match-score">
    <div class="d-flex justify-content-center align-items-center">
        <img src="../../static/img/playing_tennis.jpg" alt="img" class="tennis-img" style="max-height: 200px; object-fit: cover; max-width: 950px; object-position: top;">
    </div>
    <c:choose>
        <c:when test="${matchDto.isTieBreak}">
            <div class="alert alert-primary d-flex align-items-center justify-content-center fw-bold" role="alert">
                <svg xmlns="http://www.w3.org/2000/svg" class="bi bi-exclamation-triangle-fill me-2"
                     style="width: 1rem; height: 1rem;" viewBox="0 0 16 16" role="img" aria-label="Warning:">
                    <path d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"></path>
                </svg>
                <div>
                    Tie-break game is started ! Scoring strategy is changed
                </div>
            </div>
        </c:when>
    </c:choose>
    <section class="container match-score">
        <div class="row">
            <div class="table-wrapper rounded-4 overflow-hidden shadow-sm w-75 mx-auto">
                <table class="table table-borderless table m-0 text-center">
                    <thead class="result">
                    <tr>
                        <th class="table-text">Player</th>
                        <th class="table-text">Sets</th>
                        <th class="table-text">Games</th>
                        <th class="table-text">Points</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr class="player1">
                        <td class="table-text">${matchDto.player1().name()}</td>
                        <td class="table-text">${matchDto.player1Sets()}</td>
                        <td class="table-text">${matchDto.player1GamesInCurrentSet()}</td>
                        <td class="table-text">${matchDto.player1Points()}</td>
                        <td class="table-text">
                            <div class="d-flex align-items-center">
                                <form class="me-2" method="POST"
                                      action="${pageContext.request.contextPath}/match-score">
                                    <input type="hidden" name="uuid" value="${matchDto.matchId()}"/>
                                    <input type="hidden" name="playerId" value="${matchDto.player1().id()}"/>
                                    <button type="submit" class="btn btn-primary plus-button">
                                        +
                                    </button>
                                </form>
                            </div>
                        </td>
                    </tr>
                    <tr class="player2">
                        <td class="table-text">${matchDto.player2().name()}</td>
                        <td class="table-text">${matchDto.player2Sets()}</td>
                        <td class="table-text">${matchDto.player2GamesInCurrentSet()}</td>
                        <td class="table-text">${matchDto.player2Points()}</td>
                        <td class="table-text">
                            <div class="d-flex align-items-center">
                                <form class="me-2" method="POST"
                                      action="${pageContext.request.contextPath}/match-score">
                                    <input type="hidden" name="uuid" value="${matchDto.matchId()}"/>
                                    <input type="hidden" name="playerId" value="${matchDto.player2().id()}"/>
                                    <button type="submit" class="btn btn-primary plus-button">
                                        +
                                    </button>
                                </form>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </section>
</section>
<footer>
    <p>© Tennis Scoreboard, project from <a href="https://github.com/Ar4ik4ik">https://github.com/Ar4ik4ik</a></p>
</footer>
</body>
</html>