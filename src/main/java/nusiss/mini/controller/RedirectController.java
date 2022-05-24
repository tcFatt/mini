package nusiss.mini.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import nusiss.mini.model.CarPark;
import nusiss.mini.model.User;
import nusiss.mini.service.CarParkService;
import nusiss.mini.service.UserService;

@Controller
@RequestMapping("/secure")
public class RedirectController {

    @Autowired
    private CarParkService cpSvc;

    @Autowired
    private UserService userSvc;

    @GetMapping("/home")
    public ModelAndView getHome(HttpSession sess) {
        String username = (String)sess.getAttribute("username");
        ModelAndView mvc = new ModelAndView();
        mvc.addObject("username", username);
        mvc.setViewName("index");
        mvc.setStatus(HttpStatus.OK);        
        return mvc;
    }

    @GetMapping("/profile")
    public ModelAndView getProfile(HttpSession sess) {
        String username = (String)sess.getAttribute("username");
        ModelAndView mvc = new ModelAndView();
        mvc.addObject("username", username);
        Optional<User> opt = userSvc.findUserByUsername(username);
        if (opt.isEmpty()) {
            mvc.setViewName("error");
            mvc.setStatus(HttpStatus.NOT_FOUND);
        } else {
            User user = opt.get();
            mvc.addObject("user", user);
            mvc.setViewName("profile");
            mvc.setStatus(HttpStatus.OK);
        }
        return mvc;
    }

    @GetMapping("/deactivate")
    public ModelAndView deactivateUserAccount(@RequestParam String authusername, 
        HttpSession sess) {
        ModelAndView mvc = new ModelAndView();
        String username = (String)sess.getAttribute("username");
        Optional<User> opt = userSvc.findUserByUsername(username);
        if (opt.isEmpty()) {
            mvc.setViewName("error");
            mvc.setStatus(HttpStatus.NOT_FOUND);
        } else {
            if (authusername.trim().equals(username)) {
                userSvc.deactivateUserAccount(username);
                sess.invalidate();
                mvc.addObject("message", 
                "You have deactivated your account.");
                mvc.setViewName("login");
                mvc.setStatus(HttpStatus.OK);                
            } else {    
                User user = opt.get();
                mvc.addObject("user", user);
                mvc.addObject("message1", 
                "Invalid username! Please try again.");
                mvc.setViewName("profile");
                mvc.setStatus(HttpStatus.BAD_REQUEST);
            }
        }
        return mvc;
    }

    @GetMapping("/change")
    public ModelAndView changeUserPassword(@ModelAttribute User user, HttpSession sess) {
        String username = (String)sess.getAttribute("username");
        user.setUsername(username);
        ModelAndView mvc = new ModelAndView();
        mvc.addObject("username", username);
        Optional<User> opt = userSvc.findUserByUsername(username);
        if (opt.isEmpty()) {
            mvc.setViewName("error");
            mvc.setStatus(HttpStatus.NOT_FOUND);
        } else {
            if (!userSvc.changeUserPassword(user)) {
                mvc.addObject("message1", 
                "Invalid old password. Please try again.");
                mvc.setStatus(HttpStatus.BAD_REQUEST);
            } else {
                User u = opt.get();
                mvc.addObject("message2", 
                "You have changed your password.");
                mvc.addObject("user", u);
                mvc.setStatus(HttpStatus.OK);
            }
            mvc.setViewName("profile");
        }
        return mvc;
    }

    @GetMapping("/edit")
    public ModelAndView editUserProfile(@ModelAttribute User user, HttpSession sess) {
        String username = (String)sess.getAttribute("username");
        user.setUsername(username);
        ModelAndView mvc = new ModelAndView();
        mvc.addObject("username", username);
        if (!userSvc.changeUserNameAndEmail(user)) {
            mvc.addObject("message1", 
            "Something went wrong. Fail to change profile.");
            mvc.setStatus(HttpStatus.NOT_IMPLEMENTED);
        } else {
            mvc.addObject("message2", 
            "You have changed your profile.");
            mvc.setStatus(HttpStatus.OK);
        }
        mvc.setViewName("profile");
        return mvc;
    }

    @GetMapping("/search")
    public ModelAndView getSearchedLocation(@RequestParam String location, HttpSession sess) {
        String username = (String)sess.getAttribute("username");
        ModelAndView mvc = new ModelAndView();
        mvc.addObject("username", username);
        List<CarPark> carParkList = cpSvc.findCpInfoByAddress(location);
        if (carParkList.isEmpty()) {
            mvc.addObject("message", 
            "\"%s\" not found. Please try another location.".formatted(location));
            mvc.setStatus(HttpStatus.NOT_FOUND);
        } else {
            mvc.addObject("carParkList", carParkList);
            mvc.setStatus(HttpStatus.OK);
        }
        mvc.setViewName("index");
        return mvc;
    }

    @GetMapping("/select")
    public ModelAndView getCarParkDetails(@RequestParam String car_park_no, HttpSession sess) {
        String username = (String)sess.getAttribute("username");
        ModelAndView mvc = new ModelAndView();
        Optional<CarPark> opt = cpSvc.getCarParkInfo(car_park_no);
        if (opt.isEmpty()) {
            mvc.setViewName("error");
            mvc.setStatus(HttpStatus.NOT_FOUND);
        } else {
            CarPark cp = opt.get();
            mvc.addObject("username", username);
            mvc.addObject("carparkdata", cp);      
            mvc.setViewName("details");
            mvc.setStatus(HttpStatus.OK);
        }
        return mvc;
    }

    @GetMapping("/favourite")
    public ModelAndView getFavourites(HttpSession sess) {
        String username = (String)sess.getAttribute("username");
        List<CarPark> favouriteList = cpSvc.findFavouriteByUsername(username);
        ModelAndView mvc = new ModelAndView();
        mvc.addObject("username", username);
        mvc.addObject("favouriteAddress", favouriteList);
        mvc.setViewName("favourite");
        mvc.setStatus(HttpStatus.OK);
        return mvc;
    }

    @GetMapping("/save")
    public ModelAndView addFavourite(@RequestParam String car_park_no, HttpSession sess) {
        String username = (String)sess.getAttribute("username");
        ModelAndView mvc = new ModelAndView();
        mvc.addObject("username", username);
        Optional<CarPark> opt = cpSvc.getCarParkInfo(car_park_no);
        if (opt.isEmpty()) {
            mvc.setStatus(HttpStatus.NOT_FOUND);
            mvc.setViewName("error");
        } else {
            if (!cpSvc.tallyFavouriteByUsernameAndCarParkNo(username, car_park_no)) {
                cpSvc.addFavourite(username, car_park_no);
                mvc.addObject("message2", 
                "Car park no. %s added to favourites.".formatted(car_park_no));
                mvc.setStatus(HttpStatus.OK);
            } else {
                mvc.addObject("message1", 
                "This car park exists in the favourites!");
                mvc.setStatus(HttpStatus.BAD_REQUEST);
                }
            CarPark cp = opt.get();
            mvc.addObject("carparkdata", cp);
            mvc.setViewName("details");
        }
        return mvc;
    }

    @GetMapping("/delete")
    public ModelAndView removeFavourite(@RequestParam String car_park_no, HttpSession sess) {
        String username = (String)sess.getAttribute("username");
        ModelAndView mvc = new ModelAndView();
        mvc.addObject("username", username);
        if(!cpSvc.removeFavourite(username, car_park_no)) {
            mvc.setViewName("error");
            mvc.setStatus(HttpStatus.NOT_FOUND);
        } else {
            List<CarPark> favouriteList = cpSvc.findFavouriteByUsername(username);
            mvc.setViewName("favourite");
            mvc.addObject("favouriteAddress", favouriteList);
            mvc.addObject("message", 
            "Car park %s removed from favourites.".formatted(car_park_no));
            mvc.setStatus(HttpStatus.OK);
        }
        return mvc;
    }

}
