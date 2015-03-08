package websiteschema.mpsegment.web.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import websiteschema.mpsegment.web.exception.NotFoundException;
import websiteschema.mpsegment.web.ui.model.User;
import websiteschema.mpsegment.web.ui.model.UserDto;
import websiteschema.mpsegment.web.ui.service.UserService;

import java.util.Map;

@Controller
@RequestMapping(value = "/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("")
    public String listUser(Map<String, Object> map) {

        map.put("user", new User());
        map.put("userList", userService.listUser());

        return "user";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addUser(@ModelAttribute("user") User user, BindingResult result) {

        userService.addUser(user);

        return "redirect:.";
    }

    @RequestMapping("delete/{userId}")
    public String deleteUser(@PathVariable("userId") Integer userId) {

        userService.removeUser(userId);

        return "redirect:..";
    }

    @RequestMapping(value = "current", method = RequestMethod.GET)
    @ResponseBody
    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean authenticated = authentication.isAuthenticated();
        if (authenticated) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                int userId = Integer.valueOf(userDetails.getUsername());
                return userService.getUserById(userId).toDto();
            }
        }
        throw new NotFoundException();
    }
}
