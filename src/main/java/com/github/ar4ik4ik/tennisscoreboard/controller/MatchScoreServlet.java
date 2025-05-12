package com.github.ar4ik4ik.tennisscoreboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.ScoreIncreaseDto;
import com.github.ar4ik4ik.tennisscoreboard.service.MatchScoreCalculationService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/match-score/")
public class MatchScoreServlet extends HttpServlet {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private MatchScoreCalculationService matchScoreCalculationService;

    @Override
    public void init() {
        matchScoreCalculationService = (MatchScoreCalculationService) getServletContext()
                .getAttribute("matchScoreService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String matchId = req.getParameter("uuid");
        String playerId = req.getParameter("playerId");

        var res = matchScoreCalculationService.addPointToPlayer(new ScoreIncreaseDto(
                matchId,
                Integer.parseInt(playerId)
        ));

        resp.getWriter().write(objectMapper.writeValueAsString(res));
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
