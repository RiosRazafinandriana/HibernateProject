package org.example.DAO;

import org.example.entity.Prof;
import org.example.hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ProfDAO {
    public void addProf(Prof prof) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.save(prof);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public Prof getProf(String codeprof) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            Prof prof = session.get(Prof.class, codeprof);
            session.getTransaction().commit();
            return prof;
        } finally {
            session.close();
        }
    }

    public void updateProf(Prof prof) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.merge(prof);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public void deleteProf(String codeprof) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            Prof prof = session.get(Prof.class, codeprof);
            if (prof != null) {
                session.remove(prof);
            }
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public List<Prof> getAllProfs() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Prof> profs = null;

        try {
            session.beginTransaction();
            Query<Prof> query = session.createQuery("FROM Prof", Prof.class);

            profs = query.getResultList();

            session.getTransaction().commit();
        } finally {
            session.close();
        }

        return profs;
    }
}
