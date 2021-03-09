package DAO.repository;

import DAO.entities.CommandsLogEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class CommandsLogRepository {
    private SessionFactory sessionFactory;

    public CommandsLogRepository() {
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

    public void save(String command, String username) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(new CommandsLogEntity(command, username));
        session.getTransaction().commit();
        session.close();
    }
}
