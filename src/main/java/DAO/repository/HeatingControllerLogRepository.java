package DAO.repository;

import DAO.entities.HeatingControllerLogEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HeatingControllerLogRepository {
    private static SessionFactory sessionFactory;

    private HeatingControllerLogRepository() {
    }

    protected static void setUp() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static void save(String action){
        save(action, 0d);
    }

    public static void save(String action, Double temperature){
        final HeatingControllerLogEntity entity = new HeatingControllerLogEntity(action, temperature);
        if (sessionFactory == null){
            setUp();
        }
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(entity);
        session.getTransaction().commit();
        session.close();
    }
}
