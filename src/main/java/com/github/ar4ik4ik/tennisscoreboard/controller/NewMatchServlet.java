package com.github.ar4ik4ik.tennisscoreboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.MatchRequestDto;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.PlayerRequestDto;
import com.github.ar4ik4ik.tennisscoreboard.service.OngoingMatchesService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/new-match")
public class NewMatchServlet extends HttpServlet {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private OngoingMatchesService ongoingMatchesService;

    @Override
    public void init() {
        ongoingMatchesService = (OngoingMatchesService) getServletContext()
                .getAttribute("ongoingMatchesService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String firstPlayer = req.getParameter("firstPlayer");
        String secondPlayer = req.getParameter("secondPlayer");

        var match = ongoingMatchesService.createNewMatch(MatchRequestDto.builder()
                .firstPlayer(PlayerRequestDto.builder()
                        .name(firstPlayer)
                        .build())
                .secondPlayer(PlayerRequestDto.builder()
                        .name(secondPlayer)
                        .build())
                .build());

        resp.getWriter().write(objectMapper.writeValueAsString(match));
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
