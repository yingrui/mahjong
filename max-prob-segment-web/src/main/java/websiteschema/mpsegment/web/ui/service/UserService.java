package websiteschema.mpsegment.web.ui.service;


import websiteschema.mpsegment.web.ui.model.User;

import java.util.List;

public interface UserService {
    
    public void addUser(User user);
    public User getUserByEmail(String email);
    public List<User> listUser();
    public void removeUser(Integer id);
}
