package com.github.ar4ik4ik.tennisscoreboard.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class SessionManager {

    private final static SessionFactory SESSION_FACTORY = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        return new Configuration().configure().buildSessionFactory();
    }

    public static Session getSession() {
        return SESSION_FACTORY.getCurrentSession();
    }
}
