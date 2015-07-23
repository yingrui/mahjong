package me.yingrui.segment.web.api.service;

import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import me.yingrui.segment.web.api.model.WordItem;
import me.yingrui.segment.web.api.model.dto.WordItemDto;
import me.yingrui.segment.web.ui.model.User;
import me.yingrui.segment.web.ui.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Iterator;
import java.util.List;

@Service
public class WordItemServiceImpl implements WordItemService {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private UserService userService;
    @Autowired
    private WordItemUpdateRequestMerger merger;

    @Override
    @Transactional
    public void add(WordItem wordItem) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.getUserByEmail(userDetails.getUsername());
        wordItem.setUser(currentUser);
        em.persist(wordItem);
    }

    @Override
    @Transactional
    public void update(WordItemDto word) {
        System.out.println("username: "+SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        WordItem wordItem = getById(word.id);
        merger.merge(word).to(wordItem);
    }

    @Override
    public WordItem getById(int id) {
        return (WordItem)em.createQuery(
                "SELECT DISTINCT w From WordItem w " +
                        "LEFT OUTER JOIN FETCH w.pinyinSet pinyinSet " +
                        "LEFT OUTER JOIN FETCH w.conceptSet conceptSet " +
                        "LEFT OUTER JOIN FETCH w.wordFreqSet wordFreqSet " +
                        "LEFT OUTER JOIN FETCH conceptSet.partOfSpeech partOfSpeech " +
                        "LEFT OUTER JOIN FETCH wordFreqSet.partOfSpeech partOfSpeech2 " +
                        "LEFT OUTER JOIN FETCH w.user user " +
                        "WHERE w.id = :id")
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public Session session() {
        return ((Session) em.getDelegate()).getSessionFactory().openSession();
    }

    @Override
    public Iterator<WordItem> iterator(final Session session) {
        Query query = session.createQuery("SELECT w FROM WordItem w");
        final ScrollableResults scroll = query.scroll();
        return new Iterator<WordItem>() {
            @Override
            public boolean hasNext() {
                boolean next = scroll.next();
                if(!next) {
                    scroll.close();
                }
                return next;
            }

            @Override
            public WordItem next() {
                WordItem wordItem = (WordItem) scroll.get(0);
                session.update(wordItem);
                return wordItem;
            }
        };
    }

    @Override
    public List<WordItem> findAllByPinyin(String pinyin) {
        return em.createQuery(
                "SELECT DISTINCT w From WordItem w " +
                        "LEFT OUTER JOIN FETCH w.pinyinSet pinyinSet " +
                        "LEFT OUTER JOIN FETCH w.conceptSet conceptSet " +
                        "LEFT OUTER JOIN FETCH w.wordFreqSet wordFreqSet " +
                        "LEFT OUTER JOIN FETCH conceptSet.partOfSpeech partOfSpeech " +
                        "LEFT OUTER JOIN FETCH wordFreqSet.partOfSpeech partOfSpeech2 " +
                        "LEFT OUTER JOIN FETCH w.user user " +
                        "WHERE pinyinSet.name = :pinyin")
                .setParameter("pinyin", pinyin)
                .getResultList();
    }

    @Override
    public List<WordItem> findAllByWordHead(String wordHead) {
        return em.createQuery(
                "SELECT DISTINCT w From WordItem w " +
                        "LEFT OUTER JOIN FETCH w.pinyinSet pinyinSet " +
                        "LEFT OUTER JOIN FETCH w.conceptSet conceptSet " +
                        "LEFT OUTER JOIN FETCH w.wordFreqSet wordFreqSet " +
                        "LEFT OUTER JOIN FETCH conceptSet.partOfSpeech partOfSpeech " +
                        "LEFT OUTER JOIN FETCH wordFreqSet.partOfSpeech partOfSpeech2 " +
                        "LEFT OUTER JOIN FETCH w.user user " +
                        "WHERE w.name like :wordHead ORDER BY w.id")
                .setParameter("wordHead", wordHead.trim() + "%")
                .getResultList();
    }

    @Override
    public void remove(int id) {
        WordItem person = getById(id);
        if (null != person) {
            em.remove(person);
        }
    }
}
