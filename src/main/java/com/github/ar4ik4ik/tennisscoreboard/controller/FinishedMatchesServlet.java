package com.github.ar4ik4ik.tennisscoreboard.controller;

import com.github.ar4ik4ik.tennisscoreboard.model.dto.FinishedMatchResponseDto;
import com.github.ar4ik4ik.tennisscoreboard.service.FinishedMatchesQueryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

import static com.github.ar4ik4ik.tennisscoreboard.util.ParameterValidator.isEmpty;

@Slf4j
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
        int totalPages;

        if (pageParameter != null) {
            try {
                currentPage = Integer.parseInt(pageParameter);
            } catch (NumberFormatException e) {
                log.error("Error while parsing page parameter with value={}", pageParameter);
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Can't parse non-numeric symbols into numeric," +
                        " please check input");
            }

            if (currentPage < 1) {
                log.debug("Received page parameter is negative: {}", currentPage);
                currentPage = 1;
                log.debug("Setting default page parameter state={}", currentPage);
            }
        }

        if (isEmpty(playerNameFilter)) {
            matches = finishedMatchesQueryService.getAllFinishedMatches(currentPage, maxItems);
            totalPages = finishedMatchesQueryService.getTotalPages(maxItems);
            log.debug("Received playerNameFilter is empty: {}," +
                    " making search without parameter, found data={}, and total pages={}", playerNameFilter, matches, totalPages);
        } else {
            matches = finishedMatchesQueryService.getAllFinishedMatchesByName(playerNameFilter.trim(), currentPage, maxItems);
            totalPages = finishedMatchesQueryService.getTotalPages(maxItems, playerNameFilter.trim());
            log.debug("Received playerNameFilter is not empty: {}," +
                    " making search without parameter, found data={}, and total pages={}", playerNameFilter, matches, totalPages);
        }


        req.setAttribute("matchesList", matches);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("maxItems", maxItems);
        req.setAttribute("playerNameFilter", playerNameFilter != null ? playerNameFilter : "");
        req.getRequestDispatcher("/WEB-INF/views/match-list.jsp").forward(req, resp);
    }
}
