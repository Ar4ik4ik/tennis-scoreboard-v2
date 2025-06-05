package com.github.ar4ik4ik.tennisscoreboard.persistence.repository;

import com.github.ar4ik4ik.tennisscoreboard.persistence.entity.MatchEntity;
import com.github.ar4ik4ik.tennisscoreboard.util.SessionManager;
import org.hibernate.Session;

import java.util.List;


public class MatchRepository extends BaseRepository<Integer, MatchEntity> implements MatchRepositoryImpl{

    public MatchRepository() {
        super(MatchEntity.class);
    }

    public List<MatchEntity> findAll(int offset, int limit) {
        Session session = SessionManager.getSession();
        return session.createQuery("SELECT m " + "FROM MatchEntity m" +
                        " order by id desc", clazz)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<MatchEntity> findAllByPlayerName(String playerName, int offset, int limit) {
        Session session = SessionManager.getSession();
        return session.createQuery("SELECT m " + "FROM   MatchEntity m "
                + "WHERE  m.firstPlayer.name  = :name "
                + "   OR  m.secondPlayer.name = :name "
                + "order by id desc", MatchEntity.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .setParameter("name", playerName).getResultList();
    }

    public long getObjectCount() {
        Session session = SessionManager.getSession();
        return (long) session.createQuery("SELECT count(m) from MatchEntity m").getSingleResult();
    }

    public long getObjectCount(String playerName) {
        Session session = SessionManager.getSession();
        return (long) session.createQuery("SELECT count(m) " +
                "from MatchEntity m " +
                "where m.firstPlayer.name = :name" +
                " or m.secondPlayer.name = :name").setParameter("name", playerName).getSingleResult();
    }
}
