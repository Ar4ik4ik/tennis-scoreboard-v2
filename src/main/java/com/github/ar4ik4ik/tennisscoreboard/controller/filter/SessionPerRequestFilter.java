package com.github.ar4ik4ik.tennisscoreboard.controller.filter;

import com.github.ar4ik4ik.tennisscoreboard.util.SessionManager;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class SessionPerRequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Session session = SessionManager.getSession();
        Transaction tx = session.beginTransaction();
        try {
            filterChain.doFilter(servletRequest, servletResponse);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
