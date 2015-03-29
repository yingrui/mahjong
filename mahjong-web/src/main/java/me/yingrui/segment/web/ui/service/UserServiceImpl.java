package me.yingrui.segment.web.ui.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import me.yingrui.segment.web.ui.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @PersistenceContext
    EntityManager em;
        
    @Transactional
    public void addUser(User user) {
        em.persist(user);
    }

    @Override
    public User getUserByEmail(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);
        query.where(cb.equal(user.get("email"), email));
        return em.createQuery(query).getSingleResult();
    }

    @Override
    public User getUserById(int id) {
        return em.find(User.class, id);
    }

    @Transactional
    public List<User> listUser() {
        CriteriaQuery<User> c = em.getCriteriaBuilder().createQuery(User.class);
        c.from(User.class);
        return em.createQuery(c).getResultList();
    }

    @Transactional
    public void removeUser(Integer id) {
        User person = em.find(User.class, id);
        if (null != person) {
            em.remove(person);
        }
    }
    
}
