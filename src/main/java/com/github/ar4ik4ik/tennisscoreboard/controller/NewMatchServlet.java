package com.github.ar4ik4ik.tennisscoreboard.controller;

import com.github.ar4ik4ik.tennisscoreboard.model.dto.MatchRequestDto;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.PlayerRequestDto;
import com.github.ar4ik4ik.tennisscoreboard.service.OngoingMatchesService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import static com.github.ar4ik4ik.tennisscoreboard.util.ParameterValidator.isEmpty;

import java.io.IOException;

@Log4j2
@WebServlet(urlPatterns = "/new-match")
public class NewMatchServlet extends HttpServlet {

    private OngoingMatchesService ongoingMatchesService;

    @Override
    public void init() {
        ongoingMatchesService = (OngoingMatchesService) getServletContext()
                .getAttribute("ongoingMatchesService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String firstPlayer = req.getParameter("firstPlayerName");
        String secondPlayer = req.getParameter("secondPlayerName");

        log.debug("Creating new match with players {} and {}", firstPlayer, secondPlayer);

        if (isEmpty(firstPlayer) || isEmpty(secondPlayer) || firstPlayer.equals(secondPlayer)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.setAttribute("error", "Имена игроков должны быть заполнены и различными");
            return;
        }

        var matchUUID = ongoingMatchesService.createNewMatch(MatchRequestDto.builder()
                .firstPlayer(PlayerRequestDto.builder()
                        .name(firstPlayer)
                        .build())
                .secondPlayer(PlayerRequestDto.builder()
                        .name(secondPlayer)
                        .build())
                .build());

        resp.sendRedirect(req.getContextPath() + "/match-score?uuid=" + matchUUID);
    }
}
