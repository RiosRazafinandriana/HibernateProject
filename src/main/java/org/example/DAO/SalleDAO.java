package org.example.DAO;

import org.example.entity.Prof;
import org.example.entity.Salle;
import org.example.hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class SalleDAO {
    public void addSalle(Salle salle) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.save(salle);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public Salle getSalle(String codesal) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            Salle salle = session.get(Salle.class, codesal);
            session.getTransaction().commit();
            return salle;
        } finally {
            session.close();
        }
    }

    public String findLastCodeSal(){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String lastCodesal = null;
        try {
            session.beginTransaction();
            Query<String> query = session.createQuery(
                    "SELECT s.codeSal FROM Salle s ORDER BY CAST(SUBSTRING(s.codeSal, 2) AS int) DESC",
                    String.class
            );
            query.setMaxResults(1);
            lastCodesal = query.uniqueResult();
        }finally {
            session.close();
        }
        return lastCodesal;
    }

    public void updateSalle(Salle salle) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.merge(salle);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public void deleteSalle(String codesal) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            Salle salle = session.get(Salle.class, codesal);
            if (salle != null) {
                session.remove(salle);
            }
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public List<Salle> getAllSalles() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Salle> salles = null;

        try {
            session.beginTransaction();
            Query<Salle> query = session.createQuery("FROM Salle", Salle.class);

            salles = query.getResultList();

            session.getTransaction().commit();
        } finally {
            session.close();
        }

        return salles;
    }
}
