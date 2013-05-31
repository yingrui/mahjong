package websiteschema.mpsegment.web;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.mpsegment.web.api.model.PartOfSpeech;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class UsingFixtures extends UsingTestUtils {

    static ApplicationContext ctx = new ClassPathXmlApplicationContext("testContext.xml");
    protected static EntityManager em = resolve("entityManagerFactory", EntityManagerFactory.class).createEntityManager();
    static {
        initPartOfSpeech();
    }

    public static <T> T resolve(String bean, Class<T> clazz) {
        return ctx.getBean(bean, clazz);
    }

    protected static PartOfSpeech posN;
    protected static PartOfSpeech posT;
    protected static PartOfSpeech posUN;

    private static void initPartOfSpeech() {
        posN = addPartOfSpeech(1, "名词", "N");
        posT = addPartOfSpeech(2, "时间词", "T");
        posUN = addPartOfSpeech(44, "未登录词", "UN");
    }

    private static PartOfSpeech addPartOfSpeech(int id, String note, String pos) {
        PartOfSpeech partOfSpeech = new PartOfSpeech();
        partOfSpeech.setId(id);
        partOfSpeech.setNote(note);
        partOfSpeech.setName(pos);
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(partOfSpeech);
        transaction.commit();
        return partOfSpeech;
    }
}
