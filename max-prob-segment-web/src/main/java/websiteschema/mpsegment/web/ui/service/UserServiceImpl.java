package websiteschema.mpsegment.web.ui.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.mpsegment.web.ui.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @PersistenceContext
    EntityManager em;
        
    @Transactional
    public void addUser(User user) {
        em.persist(user);
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
