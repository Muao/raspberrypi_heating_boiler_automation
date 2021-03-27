package DAO.repository;

import DAO.entities.ModeEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class ModeRepository {

    private SessionFactory sessionFactory;

    public ModeRepository() {
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

    public String createNewMode(String modeName, String user) {
        String result = modeName;
        if (isEntityExist(modeName)) {
            result += " already exists.";
        } else {
            final ModeEntity newMode = new ModeEntity(modeName, false, user);
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(newMode);
            session.getTransaction().commit();
            session.close();
            if (isEntityExist(modeName)) {
                result += " created gracefully.";
            } else {
                result += " not created.";
            }
        }
        return result;
    }

    private boolean isEntityExist(String modeName) {
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        var createdModeList = session.createQuery("FROM ModeEntity m WHERE m.modeName = :modeName")
                .setParameter("modeName", modeName).list();
        session.close();
        return createdModeList.size() == 1;
    }
}
