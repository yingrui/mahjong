package websiteschema.mpsegment.web.ui.service;


import websiteschema.mpsegment.web.ui.model.User;

import java.util.List;

public interface UserService {
    
    public void addUser(User person);
    public List<User> listUser();
    public void removeUser(Integer id);
}
