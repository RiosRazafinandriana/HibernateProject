package org.example.DAO;

import org.example.entity.Occuper;
import org.example.entity.Prof;
import org.example.hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class OccuperDAO {

    public void addOccuper(Occuper occuper) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.save(occuper);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public Occuper getOccuper(int idOccup) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            Occuper occuper = session.get(Occuper.class, idOccup);
            session.getTransaction().commit();
            return occuper;
        } finally {
            session.close();
        }
    }

    public void updateOccuper(Occuper occuper) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.merge(occuper);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public void deleteOccuper(int idOccup) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            Occuper occuper = session.get(Occuper.class, idOccup);
            if (occuper != null) {
                session.remove(occuper);
            }
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public List<Occuper> getAllOccuperList() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Occuper> occuperList = null;

        try {
            session.beginTransaction();
            Query<Occuper> query = session.createQuery("FROM Occuper", Occuper.class);

            occuperList = query.getResultList();

            session.getTransaction().commit();
        } finally {
            session.close();
        }

        return occuperList;
    }
}
