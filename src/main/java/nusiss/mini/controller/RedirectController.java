package nusiss.mini.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/secure/{view}")
public class RedirectController {

    @RequestMapping
    public ModelAndView getSearch(@PathVariable String view, HttpSession sess) {

        String username = (String)sess.getAttribute("username");

        ModelAndView mvc = new ModelAndView();
        mvc.setViewName(view);
        mvc.addObject("username", username);
        mvc.setStatus(HttpStatus.OK);
        
        return mvc;
    }

}
