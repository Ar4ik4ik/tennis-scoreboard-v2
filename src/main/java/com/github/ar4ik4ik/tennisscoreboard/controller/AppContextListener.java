package com.github.ar4ik4ik.tennisscoreboard.controller;

import com.github.ar4ik4ik.tennisscoreboard.persistence.repository.MatchRepository;
import com.github.ar4ik4ik.tennisscoreboard.persistence.repository.PlayerRepository;
import com.github.ar4ik4ik.tennisscoreboard.service.*;
import com.github.ar4ik4ik.tennisscoreboard.util.MatchFactory;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        var playerRepo = new PlayerRepository();
        var matchRepo = new MatchRepository();

        var playerService = new PlayerManagerService(playerRepo);
        var ongoingMatchesService = new OngoingMatchesService(playerService, new MatchFactory());
        var finishedMatchesService = new FinishedMatchesPersistenceService(matchRepo);
        var matchScoreService = new MatchScoreCalculationService(ongoingMatchesService, finishedMatchesService);
        var finishedMatchesQuery = new FinishedMatchesQueryService(matchRepo);

        sce.getServletContext().setAttribute("playerService", playerService);
        sce.getServletContext().setAttribute("ongoingMatchesService", ongoingMatchesService);
        sce.getServletContext().setAttribute("finishedMatchesService", finishedMatchesService);
        sce.getServletContext().setAttribute("matchScoreService", matchScoreService);
        sce.getServletContext().setAttribute("finishedMatchesQuery", finishedMatchesQuery);
    }
}
