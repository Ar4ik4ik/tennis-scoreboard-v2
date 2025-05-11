package com.github.ar4ik4ik.tennisscoreboard.persistence.repository;

import com.github.ar4ik4ik.tennisscoreboard.persistence.entity.MatchEntity;
import com.github.ar4ik4ik.tennisscoreboard.util.SessionManager;
import org.hibernate.Session;

import java.util.List;


public class MatchRepository extends BaseRepository<Integer, MatchEntity> {

    public MatchRepository() {
        super(MatchEntity.class);
    }

    public List<MatchEntity> findAllByPlayerName(String playerName, int offset, int limit) {
        Session session = SessionManager.getSession();
        return session.createQuery("SELECT m " + "FROM   MatchEntity m "
                + "WHERE  m.firstPlayer.name  = :name "
                + "   OR  m.secondPlayer.name = :name", MatchEntity.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .setParameter("name", playerName).getResultList();
    }


}
