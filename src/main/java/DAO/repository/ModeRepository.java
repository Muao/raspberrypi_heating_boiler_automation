package DAO.repository;

import DAO.entities.ModeEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Objects;

public class ModeRepository {

    private static SessionFactory sessionFactory;

    private ModeRepository() {
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

    public static String createNewMode(String modeName, String user) {
        String result = modeName;
        if (isEntityExist(modeName)) {
            result += " already exists.";
        } else {
            final ModeEntity newMode = new ModeEntity(modeName, false, user);
            result = saveModeEntity(newMode);
        }
        return result;
    }

    private static boolean isEntityExist(String modeName) {
        return getEntity(modeName) != null;
    }

    private static String saveModeEntity(ModeEntity entity) {
        String result = entity.getModeName();

        if (sessionFactory == null) {
            setUp();
        }

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(entity);
        session.getTransaction().commit();
        session.close();

        if (isEntityExist(entity.getModeName())) {
            result += " Saving of entity was performed.";
        } else {
            result += " Saving of entity wasn't performed.";
        }
        return result;
    }

    @Nullable
    private static ModeEntity getEntity(String modeName) {
        if (sessionFactory == null) {
            setUp();
        }
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        final ModeEntity result = (ModeEntity) session.createQuery("FROM ModeEntity m WHERE m.modeName = :modeName")
                .setParameter("modeName", modeName).getSingleResult();
        session.close();
        return result;
    }

    public boolean getModeState(String modeName) {
        return Objects.requireNonNull(getEntity(modeName)).getMode();
    }

    public String setModeState(String modeName, boolean state, String user) {
        String result = modeName;
        final ModeEntity entity = getEntity(modeName);
        if (entity != null) {
            if (entity.getMode() == state) {
                result += " The same value of state already exist";
            } else {
                entity.setMode(state);
                entity.setEditDate(LocalDateTime.now());
                entity.setEditor(user);
                result = saveModeEntity(entity);
            }
        } else {
            result += " entity wasn't found";
        }
        return result;
    }

}
