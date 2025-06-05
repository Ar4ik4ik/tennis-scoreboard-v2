package com.github.ar4ik4ik.tennisscoreboard.controller;

import com.github.ar4ik4ik.tennisscoreboard.model.dto.FinishedMatchResponseDto;
import com.github.ar4ik4ik.tennisscoreboard.service.FinishedMatchesQueryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static com.github.ar4ik4ik.tennisscoreboard.util.ParameterValidator.isEmpty;

@WebServlet(urlPatterns = {"/matches"})
public class FinishedMatchesServlet extends HttpServlet {
    private FinishedMatchesQueryService finishedMatchesQueryService;

    @Override
    public void init() {
        finishedMatchesQueryService = (FinishedMatchesQueryService) getServletContext()
                .getAttribute("finishedMatchesQuery");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int currentPage = 1;
        int maxItems = 5;
        String pageParameter = req.getParameter("page");
        String playerNameFilter = req.getParameter("filter_by_player_name");
        List<FinishedMatchResponseDto> matches;

        if (pageParameter != null) {
            currentPage = Integer.parseInt(pageParameter);
            if (currentPage < 1) {
                currentPage = 1;
            }
        }

        if (isEmpty(playerNameFilter)) {
            matches = finishedMatchesQueryService.getAllFinishedMatches(currentPage, maxItems);
        } else {
            matches = finishedMatchesQueryService.getAllFinishedMatchesByName(playerNameFilter, currentPage, maxItems);
        }

        int totalPages = finishedMatchesQueryService.getTotalPages(maxItems, playerNameFilter);

        req.setAttribute("matchesList", matches);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("maxItems", maxItems);
        req.setAttribute("playerNameFilter", playerNameFilter != null ? playerNameFilter : "");
        req.getRequestDispatcher("/WEB-INF/views/match-list.jsp").forward(req, resp);
    }
}
