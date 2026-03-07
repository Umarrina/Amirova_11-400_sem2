package ru.kpfu.itis.amirova.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.amirova.model.User;

import java.util.List;

@Component
public class UserRepositoryHibernate {

    private final SessionFactory sessionFactory;

    public UserRepositoryHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from User", User.class)
                .list();
    }
}
