package nusiss.mini.controller;


import static nusiss.mini.model.ConvertUtil.convertUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import nusiss.mini.model.User;
import nusiss.mini.repository.UserRepo;
import nusiss.mini.service.UserService;

@Controller
@RequestMapping("/")
public class UserController{

    @Autowired
    private UserService userSvc;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/register")
    public String getRegistration() {
        return "registration";
    }

    @GetMapping
    public String getIndex() {
        return "index";
    }

    @GetMapping("/logout")
    public String getLogout(HttpSession sess) {
        sess.invalidate();
        return "index";
    }

    @PostMapping("/login")
    public ModelAndView postAuthenticate(@RequestBody MultiValueMap<String, String> payload
            , HttpSession sess, HttpServletRequest request, HttpServletResponse response) {

        String username = payload.getFirst("username");
        String password = payload.getFirst("password");

        ModelAndView mvc = new ModelAndView();

        if (!userSvc.authenticate(username, password)) {
            mvc.addObject("message", "Username or Password are incorrect! Please try again.");
            mvc.setStatus(HttpStatus.BAD_REQUEST);
            mvc.setViewName("index");
        } else {
            sess.setAttribute("username", username);
            mvc = new ModelAndView("redirect:/secure/home");
        }   
        return mvc;   
    }

    @PostMapping("/register")
    public ModelAndView postRegistration(@RequestBody MultiValueMap<String, String> form) {

        User user = convertUser(form);

        ModelAndView mvc = new ModelAndView();

        if (!(userSvc.filterRegistration(user.getUsername(), user.getEmail()))) {
            mvc.addObject("message", "%s or %s is exists.".formatted(user.getUsername(), user.getEmail()));
        } else {
            userRepo.insertUser(user);
            mvc.addObject("message", "Username %s successful registered.".formatted(user.getUsername()));
        }

        mvc.setViewName("registration");

        return mvc;
    }
    
}