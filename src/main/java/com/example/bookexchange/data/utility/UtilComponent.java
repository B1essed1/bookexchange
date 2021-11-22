package com.example.bookexchange.data.utility;

import com.example.bookexchange.data.model.system.User;
import com.example.bookexchange.data.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Component
public class UtilComponent {

    @PersistenceContext
    EntityManager em;

    @Autowired
    UserService userService;

    public User getCurrentUser() {
        Query query = em.createQuery("select u from User u where u.username = ?1");
        query.setParameter(1, SecurityContextHolder.getContext().getAuthentication().getName());
        return (User) query.getSingleResult();
    }
}
