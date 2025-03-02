package org.example.hibernate;

import lombok.Getter;
import org.example.entity.Prof;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    @Getter
    private static SessionFactory sessionFactory;

    static {
        try {
            // Création de la session factory avec la configuration Hibernate
            sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Prof.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
