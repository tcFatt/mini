package nusiss.mini.controller;

import static nusiss.mini.model.ConvertUtil.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import nusiss.mini.model.User;
import nusiss.mini.service.UserService;

@Controller
@RequestMapping("/")
public class UserController{

    @Autowired
    private UserService userSvc;

    @GetMapping({"", "/login"})
    public String getLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegistration() {
        return "registration";
    }

    @GetMapping("/reset")
    public String getReset() {
        return "reset";
    }

    @GetMapping("/logout")
    public ModelAndView getLogout(HttpSession sess) {
        sess.invalidate();
        ModelAndView mvc = new ModelAndView();
        mvc.addObject("message", 
        "You have logged out successfully.");
        mvc.setViewName("login");
        mvc.setStatus(HttpStatus.OK);
        return mvc;
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView postRegistration(@RequestBody MultiValueMap<String, String> form) {
        User user = convertUserByForm(form);
        ModelAndView mvc = new ModelAndView();
        if (!(userSvc.checkExistingUserByUsernameOrEmail(user.getUsername(), user.getEmail()))) {
            mvc.addObject("message1", 
            "Username or email is exists. Please try again.");
            mvc.setStatus(HttpStatus.BAD_REQUEST);
        } else {
            userSvc.addNewUser(user);
            mvc.addObject("message2", "You have registered successfully.");
            mvc.setStatus(HttpStatus.OK);
        }
        mvc.setViewName("registration");
        return mvc;
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView postAuthenticate(@RequestBody MultiValueMap<String, String> payload
            , HttpSession sess, HttpServletRequest request, HttpServletResponse response) {
        String username = payload.getFirst("username");
        String password = payload.getFirst("password");
        ModelAndView mvc = new ModelAndView();
        if (!userSvc.authenticateLogin(username, password)) {
            mvc.addObject("message", 
            "Invalid username or password! Please try again.");
            mvc.setStatus(HttpStatus.BAD_REQUEST);
            mvc.setViewName("login");
        } else {
            sess.setAttribute("username", username);
            mvc.setStatus(HttpStatus.OK);
            mvc = new ModelAndView("redirect:/secure/home");
        }   
        return mvc;   
    }

    @GetMapping("/profile")
    public ModelAndView getProfile() {
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("redirect:/secure/profile");
        return mvc;
    }

    @PostMapping(path = "/edit", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView editProfile(@RequestBody MultiValueMap<String, String> form, 
        RedirectAttributesModelMap model) {
        User user = convertUserByForm(form);
        model.addFlashAttribute(user);
        ModelAndView mvc = new ModelAndView("redirect:/secure/edit", model);
        return mvc;
    }

    @PostMapping(path = "/change", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView changePassword(@RequestBody MultiValueMap<String, String> form, 
        RedirectAttributesModelMap model) {
        User user = convertUserByForm(form);
        model.addFlashAttribute(user);
        ModelAndView mvc = new ModelAndView("redirect:/secure/change");
        return mvc;
    }

    @PostMapping(path = "/deactivate", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView deactivateUser(@RequestParam String username, ModelMap model) {
        model.addAttribute("authusername", username);
        ModelAndView mvc = new ModelAndView("redirect:/secure/deactivate", model);
        return mvc;
    }

    @PostMapping(path = "/reset", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView resetPassword(@RequestBody MultiValueMap<String, String> form) {
        User user = convertUserByForm(form);
        ModelAndView mvc = new ModelAndView();
        if (!userSvc.resetUserPassword(user)) {
            mvc.addObject("message1", 
            "Invalid username or email! Please try again.");
            mvc.setStatus(HttpStatus.BAD_REQUEST);
        } else {
            mvc.addObject("message2", 
            "You have reset your password. Please log in with the new password.");
            mvc.setStatus(HttpStatus.OK);
        }
        mvc.setViewName("reset");
        return mvc;
    }

}