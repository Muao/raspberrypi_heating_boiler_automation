package DAO.repository;

import DAO.entities.ComPortDataEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ComPortRepository {
    private SessionFactory sessionFactory;
    private static ComPortRepository instance;

    private ComPortRepository() {
        setUp();
    }

    public static ComPortRepository getInstance() {
        if (instance == null){
            instance = new ComPortRepository();
        }
        return instance;
    }

    public void save(ComPortDataEntity comportData) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(comportData);
        session.getTransaction().commit();
        session.close();
    }

    public List<ComPortDataEntity> get(LocalDateTime start, LocalDateTime end) {
        final Session session = sessionFactory.openSession();

        session.beginTransaction();
        final var hql = "FROM ComPortDataEntity c WHERE c.date >= :start and c.date <=:end";
        final var query = session.createQuery(hql)
                .setParameter("start", start)
                .setParameter("end", end);

        final var resultList = query.getResultList();
        session.close();

        return (ArrayList<ComPortDataEntity>) resultList;
    }

    private void setUp() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
