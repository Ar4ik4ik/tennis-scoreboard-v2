package com.github.ar4ik4ik.tennisscoreboard.persistence.repository;

import com.github.ar4ik4ik.tennisscoreboard.persistence.entity.MatchEntity;
import com.github.ar4ik4ik.tennisscoreboard.util.SessionManager;
import org.hibernate.Session;

import java.util.List;


public class MatchRepository extends BaseRepository<Integer, MatchEntity> implements MatchRepositoryImpl {

    public MatchRepository() {
        super(MatchEntity.class);
    }

    public List<MatchEntity> findAll(int offset, int limit) {
        Session session = SessionManager.getSession();
        return session.createQuery("SELECT m " + "FROM MatchEntity m " +
                                "JOIN FETCH m.firstPlayer fp " +
                                "JOIN FETCH m.secondPlayer sp " +
                                "JOIN FETCH m.winner w" +
                        " order by m.id desc", clazz)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<MatchEntity> findAllByPlayerName(String playerName, int offset, int limit) {
        Session session = SessionManager.getSession();
        return session.createQuery("SELECT m " + "FROM MatchEntity m " +
                        "JOIN FETCH m.firstPlayer fp " +
                        "JOIN FETCH m.secondPlayer sp " +
                        "JOIN FETCH m.winner w "
                        + "where lower(fp.name) LIKE :name "
                        + "OR lower(sp.name) LIKE :name "
                        + "order by m.id desc", MatchEntity.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .setParameter("name", "%" + playerName.toLowerCase() + "%").getResultList();
    }

    public long getObjectCount() {
        Session session = SessionManager.getSession();
        return session.createQuery("SELECT count(m) from MatchEntity m", Long.class)
                .getSingleResult();
    }

    public long getObjectCount(String playerName) {
        Session session = SessionManager.getSession();
        return session.createQuery("SELECT count(m) " +
                        "from MatchEntity m " +
                        "where lower(m.firstPlayer.name) LIKE :name" +
                        " or lower(m.secondPlayer.name) LIKE :name", Long.class)
                .setParameter("name", "%" + playerName.toLowerCase() + "%").getSingleResult();
    }
}
