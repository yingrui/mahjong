package me.yingrui.segment.web.ui.service;


import me.yingrui.segment.web.ui.model.User;

import java.util.List;

public interface UserService {
    
    public void addUser(User user);
    public User getUserByEmail(String email);
    public User getUserById(int id);
    public List<User> listUser();
    public void removeUser(Integer id);
}
