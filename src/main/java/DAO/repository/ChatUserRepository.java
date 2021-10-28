package DAO.repository;

import DAO.entities.ChatUserEntity;
import DAO.entities.ComPortDataEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public enum ChatUserRepository {
    INSTANCE;

    private SessionFactory sessionFactory;

    private ChatUserRepository(){
        setUp();
    }

    public void save(User user, String chatId) throws HibernateException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(new ChatUserEntity(user.getId(), user.getUserName(), chatId));
        session.getTransaction().commit();
        session.close();
    }

    public List<ChatUserEntity> getAll() {
        final Session session = sessionFactory.openSession();
        return session.createQuery("SELECT user FROM ChatUserEntity user", ChatUserEntity.class).getResultList();
    }

    @Nullable
    public ChatUserEntity get(String chatId){
        ChatUserEntity chatUserEntity = null;
        final Session session = sessionFactory.openSession();

        session.beginTransaction();
        final var query = session.createQuery("from ChatUserEntity user where user.chatId = :chatId ");
        query.setParameter("chatId", chatId);
        final var list = query.getResultList();
        session.close();

        if(list.size() > 0){
            chatUserEntity = (ChatUserEntity) list.get(0);
        }

        return chatUserEntity;
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
}
