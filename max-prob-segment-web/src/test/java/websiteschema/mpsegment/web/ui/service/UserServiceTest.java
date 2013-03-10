package websiteschema.mpsegment.web.ui.service;

import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import websiteschema.mpsegment.web.UsingFixtures;
import websiteschema.mpsegment.web.ui.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.junit.Assert.*;

public class UserServiceTest extends UsingFixtures {

    private UserService userService = resolve("userServiceImpl", UserService.class);

    private EntityManager em = resolve("entityManagerFactory", EntityManagerFactory.class).createEntityManager();

    @Test
    public void should_add_user_in_database() {
        String firstName = uniq("Yingrui");
        String lastName = uniq("Feng");
        String email = uniq("a@b.com");

        User user = addUser(firstName, lastName, email);

        User userInDatabase = em.find(User.class, user.getId());
        assertEquals(firstName, userInDatabase.getFirstName());
        assertEquals(lastName, userInDatabase.getLastName());
    }

    @Test
    public void should_remove_user_by_id_from_database() {
        String firstName = uniq("Yingrui");
        String lastName = uniq("Feng");
        String email = uniq("a@b.com");

        User user = addUser(firstName, lastName, email);

        userService.removeUser(user.getId());
        User userInDatabase = em.find(User.class, user.getId());
        assertNull(userInDatabase);
    }

    @Test
    public void should_list_all_users() {
        List<User> list = userService.listUser();

        String user1FirstName = uniq("Yingrui");
        String user1LastName = uniq("Feng");
        String user1Email = uniq("a@b.com");
        String user2FirstName = uniq("Yingrui");
        String user2LastName = uniq("Feng");
        String user2Email = uniq("a@b.com");

        User user1 = addUser(user1FirstName, user1LastName, user1Email);
        User user2 = addUser(user2FirstName, user2LastName, user2Email);

        List<User> users = userService.listUser();
        int userCount = 0;
        for (User u : users) {
            if (equals(u, user1)) {
                userCount++;
            }
            if (equals(u, user2)) {
                userCount++;
            }
        }
        assertEquals(2, userCount);
    }

    @Test
    public void should_be_unique_user() {
        List<User> list = userService.listUser();

        String user1FirstName = uniq("Yingrui");
        String user1LastName = uniq("Feng");
        String email = uniq("a@b.com");
        String user2FirstName = uniq("Yingrui");
        String user2LastName = uniq("Feng");

        addUser(user1FirstName, user1LastName, email);
        try {
            addUser(user2FirstName, user2LastName, email);
            fail();
        } catch (DataIntegrityViolationException exception) {
        }
    }

    private boolean equals(User user, User other) {
        return other.getFirstName().equals(user.getFirstName()) && other.getLastName().equals(user.getLastName());
    }

    private User addUser(String firstName, String lastName, String email) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        userService.addUser(user);
        return user;
    }
}
