package websiteschema.mpsegment.web.ui.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.mpsegment.web.ui.model.User;
import websiteschema.mpsegment.web.ui.model.User_;

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
        query.where(cb.equal(user.get(User_.email), email));
        return em.createQuery(query).getSingleResult();
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
