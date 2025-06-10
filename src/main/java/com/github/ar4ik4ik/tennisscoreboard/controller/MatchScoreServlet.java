package com.github.ar4ik4ik.tennisscoreboard.controller;

import com.github.ar4ik4ik.tennisscoreboard.exceptions.MatchNotFoundException;
import com.github.ar4ik4ik.tennisscoreboard.exceptions.MatchPersistenceException;
import com.github.ar4ik4ik.tennisscoreboard.exceptions.PlayerNotFoundException;
import com.github.ar4ik4ik.tennisscoreboard.model.State;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.ScoreIncreaseDto;
import com.github.ar4ik4ik.tennisscoreboard.service.MatchScoreCalculationService;
import com.github.ar4ik4ik.tennisscoreboard.service.OngoingMatchesService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

import static com.github.ar4ik4ik.tennisscoreboard.util.ParameterValidator.isEmpty;

@Slf4j
@WebServlet(urlPatterns = "/match-score")
public class MatchScoreServlet extends HttpServlet {

    private MatchScoreCalculationService matchScoreCalculationService;
    private OngoingMatchesService ongoingMatchesService;

    @Override
    public void init() {
        matchScoreCalculationService = (MatchScoreCalculationService) getServletContext()
                .getAttribute("matchScoreService");
        ongoingMatchesService = (OngoingMatchesService) getServletContext()
                .getAttribute("ongoingMatchesService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String matchId = req.getParameter("uuid");
        String playerId = req.getParameter("playerId");

        log.info("Adding point to player with ID: {}, in match with ID: {}", playerId, matchId);

        if (isEmpty(matchId) || isEmpty(playerId)) {
            log.error("Error while adding point to player with parameters: playerId={}, matchId={}", playerId, matchId);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            var updatedMatch = matchScoreCalculationService.addPointToPlayer(new ScoreIncreaseDto(
                    matchId,
                    Integer.parseInt(playerId)
            ));
            log.info("Success adding point updated match state={}", updatedMatch);
            if (Objects.equals(updatedMatch.matchState().name(), State.FINISHED.name())) {
                log.info("Match with UUID={} is finished", matchId);
                req.setAttribute("matchDto", updatedMatch);
                req.getRequestDispatcher("/WEB-INF/views/finished-page.jsp").forward(req, resp);
            } else {
                log.info("Match with UUID={} not finished, sending redirect", matchId);
                resp.sendRedirect(req.getContextPath() + "/match-score?uuid=" + matchId);
            }
        } catch (PlayerNotFoundException | MatchNotFoundException e) {
            log.error("Player with id={}, or match with id={} not found", playerId, matchId);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (MatchPersistenceException | Exception e) {
            log.error("Internal Error, cause: {}", e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var uuid = req.getParameter("uuid");
        if (isEmpty(uuid)) {
            log.error("Error while getting match with id={}", uuid);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Match id is empty");
            return;
        }

        try {
            var match = ongoingMatchesService.getOngoingMatch(uuid);
            req.setAttribute("matchDto", match);
            req.getRequestDispatcher("/WEB-INF/views/match-score.jsp").forward(req, resp);
        } catch (MatchNotFoundException e) {
            log.error("Error while getting match, match with id={} not found", uuid);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error while getting match, cause: {}", e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
