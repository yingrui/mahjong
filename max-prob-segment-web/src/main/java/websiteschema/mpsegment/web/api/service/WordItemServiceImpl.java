package websiteschema.mpsegment.web.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.mpsegment.web.api.model.WordItem;
import websiteschema.mpsegment.web.ui.model.User;
import websiteschema.mpsegment.web.ui.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Service
public class WordItemServiceImpl implements WordItemService {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void add(WordItem wordItem) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.getUserByEmail(userDetails.getUsername());
        wordItem.setUser(currentUser);
        em.persist(wordItem);
    }

    @Override
    public WordItem getById(int id) {
        return em.find(WordItem.class, id);
    }

    @Override
    public List<WordItem> list() {
        CriteriaQuery<WordItem> c = em.getCriteriaBuilder().createQuery(WordItem.class);
        c.from(WordItem.class);
        return em.createQuery(c).getResultList();
    }

    @Override
    public void remove(int id) {
        WordItem person = getById(id);
        if (null != person) {
            em.remove(person);
        }
    }
}
