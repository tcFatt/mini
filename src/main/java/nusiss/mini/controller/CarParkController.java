package nusiss.mini.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class CarParkController {

    @GetMapping("/home")
    public ModelAndView getHome() {
        ModelAndView mvc = new ModelAndView("redirect:/secure/home");
        return mvc;
    }

    @GetMapping("/favourite")
    public ModelAndView getFavouritesCarPark(){
        ModelAndView mvc = new ModelAndView("redirect:/secure/favourite");
        return mvc;
    }

    @PostMapping(path = "/search", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView postSearchLocation(
        @RequestParam(name="location", required=true) String location, ModelMap model) {        
        model.addAttribute("location", location.trim());
        ModelAndView mvc = new ModelAndView("redirect:/secure/search", model);
        return mvc;      
    }

    @PostMapping(path = "/select", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView postSelectedCarPark(@RequestParam(name="car_park_no", required=true) 
        String carParkNo, ModelMap model) throws IOException{   
        model.addAttribute("car_park_no", carParkNo);
        ModelAndView mvc = new ModelAndView("redirect:/secure/select", model);
        return mvc;
    }

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView postAddFavourite(@RequestParam(name="car_park_no", required=true) 
        String carParkNo, ModelMap model) throws IOException{
        model.addAttribute("car_park_no", carParkNo);
        ModelAndView mvc = new ModelAndView("redirect:/secure/save", model);
        return mvc;
    }

    @PostMapping(path = "/delete", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView postRemoveFavourite(@RequestParam(name="car_park_no", required=true) 
        String carParkNo, ModelMap model) throws IOException{
        model.addAttribute("car_park_no", carParkNo);
        ModelAndView mvc = new ModelAndView("redirect:/secure/delete", model);
        return mvc;
    }

}