package DAO.repository;

import DAO.entities.ComPortDataEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import utilites.ComportData;

public class ComPortRepository {
    private SessionFactory sessionFactory;

    public ComPortRepository() {
        setUp();
    }

    protected void setUp() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public void save(ComportData comportData) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(new ComPortDataEntity(comportData));
        session.getTransaction().commit();
        session.close();
    }
}
