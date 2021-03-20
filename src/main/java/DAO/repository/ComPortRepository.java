package DAO.repository;

import DAO.entities.ComPortDataEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import utilites.CalendarUtility;

import java.time.LocalDateTime;

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

    public void save(ComPortDataEntity comportData) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(comportData);
        session.getTransaction().commit();
        session.close();
    }

    public void getLastNightData(){
        final LocalDateTime[] localDateTimes = CalendarUtility.previousNight();
        LocalDateTime start = localDateTimes[0];
        LocalDateTime end = localDateTimes[1];
        System.out.println(start + "--> " + end);
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        final var hql = "FROM ComPortDataEntity c WHERE c.date >= :start and c.date <=:end";
        final var query = session.createQuery(hql)
                .setParameter("start", start)
                .setParameter("end", end);
        var resultList = query.getResultList();
        session.close();
        System.out.println(resultList.toString());
    }
}
