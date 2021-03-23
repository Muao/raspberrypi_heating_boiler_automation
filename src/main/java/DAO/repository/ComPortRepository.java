package DAO.repository;

import DAO.entities.ComPortDataEntity;
import DTO.ComPortDataMinMaxTemp;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import utilites.CalendarUtility;
import utilites.ComPortDataUtility;

import java.time.LocalDate;
import java.util.ArrayList;

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

    public ComPortDataMinMaxTemp getLastNightData(){
        final var localDateTimes = CalendarUtility.previousNight();
        final var start = localDateTimes[0];
        final var end = localDateTimes[1];
        System.out.println(start + "--> " + end);
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        final var hql = "FROM ComPortDataEntity c WHERE c.date >= :start and c.date <=:end";
        final var query = session.createQuery(hql)
                .setParameter("start", start)
                .setParameter("end", end);
        var resultList = query.getResultList();
        session.close();
        final ComPortDataMinMaxTemp averageObject = ComPortDataUtility.getAverageObject((ArrayList<ComPortDataEntity>) resultList);
        averageObject.setDate(end);
        return averageObject;
    }

    public ComPortDataMinMaxTemp getAverage24HourData(LocalDate date){
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        final var hql = "FROM ComPortDataEntity c WHERE c.date >= :start and c.date <=:end";
        final var query = session.createQuery(hql)
                .setParameter("start", date.atStartOfDay())
                .setParameter("end", date.atStartOfDay().plusDays(1));

        final var resultList = query.getResultList();
        session.close();
        final var averageObject = ComPortDataUtility.getAverageObject((ArrayList<ComPortDataEntity>) resultList);
        averageObject.setDate(date.atStartOfDay());
        return ComPortDataUtility.getDailyPower(averageObject);

    }
}
