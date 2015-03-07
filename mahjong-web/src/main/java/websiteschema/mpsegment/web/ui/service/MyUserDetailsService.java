package websiteschema.mpsegment.web.ui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.mpsegment.web.ui.model.User;

import java.util.HashSet;
import java.util.Set;

@Service("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserDetails loadUserByUsername(String username)
            throws DataAccessException {
        System.out.println(username);
        User user = userService.getUserByEmail(username);
        if (null != user) {
            System.out.println(user.getId() + " " + user.getEmail());

            Set<GrantedAuthority> auth = new HashSet<GrantedAuthority>();

                auth.add(new GrantedAuthorityImpl("ROLE_ADMIN"));

            return new org.springframework.security.core.userdetails.User(
                    String.valueOf(user.getId()),
                    user.getPassword(),
                    true,
                    true,
                    true,
                    true,
                    auth);
        } else {
            return new org.springframework.security.core.userdetails.User(
                    username,
                    "",
                    true,
                    true,
                    true,
                    true,
                    new HashSet<GrantedAuthority>());
        }
    }
}
