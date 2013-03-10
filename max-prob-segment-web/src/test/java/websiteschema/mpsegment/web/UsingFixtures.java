package websiteschema.mpsegment.web;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UsingFixtures extends UsingTestUtils {

    static ApplicationContext ctx = new ClassPathXmlApplicationContext("testContext.xml");

    public <T> T resolve(String bean, Class<T> clazz) {
        return ctx.getBean(bean, clazz);
    }
}
