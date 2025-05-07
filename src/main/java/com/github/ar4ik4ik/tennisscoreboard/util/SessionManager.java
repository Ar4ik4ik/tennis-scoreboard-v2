package com.github.ar4ik4ik.tennisscoreboard.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.lang.reflect.Proxy;

public class SessionManager {

    private final static SessionFactory SESSION_FACTORY = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure();
        return configuration.buildSessionFactory();
    }

    public static Session getSession() {
        return (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[]{Session.class},
                ((proxy, method, args) ->
                        method.invoke(SESSION_FACTORY.getCurrentSession(), args)));
    }
}
