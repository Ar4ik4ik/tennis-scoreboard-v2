package com.github.ar4ik4ik.tennisscoreboard.controller;

import com.github.ar4ik4ik.tennisscoreboard.exceptions.MatchNotFoundException;
import com.github.ar4ik4ik.tennisscoreboard.exceptions.MatchPersistenceException;
import com.github.ar4ik4ik.tennisscoreboard.exceptions.PlayerNotFoundException;
import com.github.ar4ik4ik.tennisscoreboard.model.State;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.OngoingMatchResponseDto;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.ScoreIncreaseDto;
import com.github.ar4ik4ik.tennisscoreboard.service.MatchScoreCalculationService;
import com.github.ar4ik4ik.tennisscoreboard.service.OngoingMatchesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static com.github.ar4ik4ik.tennisscoreboard.util.ParameterValidator.isEmpty;

import java.io.IOException;
import java.util.Objects;

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

        if (isEmpty(matchId) || isEmpty(playerId)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            var updatedMatch = matchScoreCalculationService.addPointToPlayer(new ScoreIncreaseDto(
                    matchId,
                    Integer.parseInt(playerId)
            ));

            if (Objects.equals(updatedMatch.matchState().name(), State.FINISHED.name())) {
                req.setAttribute("matchDto", updatedMatch);
                req.getRequestDispatcher("/WEB-INF/views/finished-page.jsp").forward(req, resp);
            } else {
                resp.sendRedirect(req.getContextPath() + "/match-score?uuid=" + matchId);
            }
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (PlayerNotFoundException | MatchNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (MatchPersistenceException | Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var uuid = req.getParameter("uuid");
        if (isEmpty(uuid)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        OngoingMatchResponseDto match = null;
        try {
            match = ongoingMatchesService.getOngoingMatch(uuid);
            req.setAttribute("matchDto", match);
            req.getRequestDispatcher("/WEB-INF/views/match-score.jsp").forward(req, resp);
        } catch (MatchNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
