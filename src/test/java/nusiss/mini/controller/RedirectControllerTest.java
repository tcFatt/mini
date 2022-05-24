package nusiss.mini.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import nusiss.mini.model.User;
import nusiss.mini.repository.Queries;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RedirectControllerTest implements Queries{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockHttpSession sess;

    @Autowired
    private JdbcTemplate template;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        sess = new MockHttpSession();
        sess.setAttribute("username", "sun");
        // some of the test will change session username to "nus", therefore apply @BeforeEach.
    }

    @AfterAll
    public void  cleanup() {
        template.update(SQL_INSERT_USER, "sun", "Sun", "sun@gmail.com", "sun");
        template.update(SQL_INSERT_FAVOURITE, "sun", "MM6");
    }

    @Order(1)
    @Test
    void testAddFavourite() throws Exception {
        mockMvc.perform(get("/secure/save")
                .param("car_park_no", "MM3")
                .session(sess))
                .andExpect(model().attribute("message2", "Car park no. MM3 added to favourites."))
                .andExpect((view().name("details")))
                .andExpect(status().isOk());
    }

    @Order(2)
    @Test
    void testAddFavouriteShouldFail() throws Exception {
        mockMvc.perform(get("/secure/save")
                .param("car_park_no", "MM3")
                .session(sess))
                .andExpect(model().attribute("message1", "This car park exists in the favourites!"))
                .andExpect((view().name("details")))
                .andExpect(status().isBadRequest()); 
    }

    @Order(3)
    @Test
    void testAddFavouriteShouldFail2() throws Exception {
        mockMvc.perform(get("/secure/save")
                .param("car_park_no", "MM3MM3")
                .session(sess))
                .andExpect(content().string(containsString("Back to homepage?")))
                .andExpect((view().name("error")))
                .andExpect(status().isNotFound());
    }

    @Order(4)
    @Test
    void testRemoveFavourite() throws Exception {
        mockMvc.perform(get("/secure/delete")
                .param("car_park_no", "MM3")
                .session(sess))
                .andExpect(model().attribute("message", "Car park MM3 removed from favourites."))
                .andExpect(model().attributeExists("favouriteAddress"))
                .andExpect((view().name("favourite")))
                .andExpect(status().isOk());
    }

    @Order(5)
    @Test
    void testRemoveFavouriteShouldFail() throws Exception {
        mockMvc.perform(get("/secure/delete")
                .param("car_park_no", "MM3MM3")
                .session(sess))
                .andExpect(content().string(containsString("Back to homepage?")))
                .andExpect((view().name("error")))
                .andExpect(status().isNotFound());
    }

    @Order(6)
    @Test
    void testChangeUserPassword() throws Exception {
        User u = new User();
        u.setUsername("sun");
        u.setName("Sun");
        u.setEmail("sun@gmail.com");
        u.setPassword("sun");
        u.setNewPassword("sun");
        mockMvc.perform(get("/secure/change")
                .flashAttr("user", u)
                .session(sess))
                .andExpect(model().attribute("message2", "You have changed your password."))
                .andExpect((view().name("profile")))
                .andExpect(status().isOk());
    }

    @Order(7)
    @Test
    void testChangeUserPasswordShouldFail() throws Exception {
        User u = new User();
        u.setUsername("sun");
        u.setName("Sun");
        u.setEmail("sun@gmail.com");
        u.setPassword("wrong");
        u.setNewPassword("sun");
        mockMvc.perform(get("/secure/change")
                .flashAttr("user", u)
                .session(sess))
                .andExpect(model().attribute("message1", "Invalid old password. Please try again."))
                .andExpect((view().name("profile")))
                .andExpect(status().isBadRequest());
    }

    @Order(8)
    @Test
    void testChangeUserPasswordShouldFail2() throws Exception {
        mockMvc.perform(get("/secure/change")
                //.flashAttr("user", u)
                .session(sess)
                .sessionAttr("username","nus"))
                .andExpect(content().string(containsString("Back to homepage?")))
                .andExpect((view().name("error")))
                .andExpect(status().isNotFound());
    }


    @Order(9)
    @Test
    void testEditUserProfile() throws Exception {
        User u = new User();
        u.setName("Sun");
        u.setEmail("sun@gmail.com");

        mockMvc.perform(get("/secure/edit")
                .flashAttr("user", u)
                .session(sess))
                .andExpect(model().attribute("message2", "You have changed your profile."))
                .andExpect((view().name("profile")))
                .andExpect(status().isOk());
    }

    @Order(10)
    @Test
    void testEditUserProfileShouldFail() throws Exception {
        User u = new User();
        u.setName("Sun");
        u.setEmail("sun@gmail.com");
        mockMvc.perform(get("/secure/edit")
                .flashAttr("user", u)
                .session(sess)
                .sessionAttr("username","nus"))
                .andExpect(model().attribute("message1", "Something went wrong. Fail to change profile."))
                .andExpect((view().name("profile")))
                .andExpect(status().isNotImplemented());
    }

    @Order(11)
    @Test
    void testGetCarParkDetails() throws Exception {
        mockMvc.perform(get("/secure/select")
                .param("car_park_no", "MM3")
                .session(sess))
                .andExpect(model().attributeExists("carparkdata"))
                .andExpect((view().name("details")))
                .andExpect(status().isOk());
    }

    @Order(12)
    @Test
    void testGetCarParkDetailsShouldFail() throws Exception {
        mockMvc.perform(get("/secure/select")
                .param("car_park_no", "3MMM3") // 3MMM3 not exists
                .session(sess))
                .andExpect(content().string(containsString("Back to homepage?")))
                .andExpect((view().name("error")))
                .andExpect(status().isNotFound());
    }

    @Order(13)
    @Test
    void testGetFavourites() throws Exception {
        mockMvc.perform(get("/secure/favourite")
                .session(sess))
                .andExpect((view().name("favourite")))
                .andExpect(status().isOk());
    }

    @Order(14)
    @Test
    void testGetHome() throws Exception {
        mockMvc.perform(get("/secure/home")
                .session(sess))
                .andExpect((view().name("index")))
                .andExpect(status().isOk());
    }
    
    @Order(15)
    @Test
    void testGetProfile() throws Exception {
        mockMvc.perform(get("/secure/profile")
                .session(sess))
                .andExpect((view().name("profile")))
                .andExpect(status().isOk());
    }

    @Order(16)
    @Test
    void testGetProfileShouldFail() throws Exception {
        mockMvc.perform(get("/secure/profile")
                .session(sess)
                .sessionAttr("username","nus")) // invalid username
                .andExpect(content().string(containsString("Back to homepage?")))
                .andExpect((view().name("error")))
                .andExpect(status().isNotFound());      
    }

    @Order(17)
    @Test
    void testGetSearchedLocation() throws Exception {
        String location = "jurong";
        mockMvc.perform(get("/secure/search")
                .param("location", location)
                .session(sess))
                .andExpect(model().attributeExists("carParkList"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Order(18)
    @Test
    void testGetSearchedLocationShouldNotFound() throws Exception {
        String location = "abcdefg";
        String message = "\"abcdefg\" not found. Please try another location.";
        mockMvc.perform(get("/secure/search")
                .param("location", location)
                .session(sess))
                .andExpect(model().attribute("message", message))
                .andExpect(status().isNotFound())
                .andExpect(view().name("index"));
    }

    @Order(19)
    @Test
    void testDeactivateUserAccountShouldFail() throws Exception {
        mockMvc.perform(get("/secure/deactivate")
                .param("authusername", "nus") // wrong username, suppose sun
                .session(sess))
                .andExpect(model().attribute("message1", 
                "Invalid username! Please try again."))
                .andExpect((view().name("profile")))
                .andExpect(status().isBadRequest());
    }

    @Order(20)
    @Test
    void testDeactivateUserAccountShouldFail2() throws Exception {
        mockMvc.perform(get("/secure/deactivate")
                .param("authusername", "sun")
                .session(sess)
                .sessionAttr("username","nus")) // unexpected wrong session username, cause error
                .andExpect(content().string(containsString("Back to homepage?")))
                .andExpect((view().name("error")))
                .andExpect(status().isNotFound());
    }

    @Order(21)
    @Test
    void testDeactivateUserAccountShouldPass() throws Exception {
        mockMvc.perform(get("/secure/deactivate")
                .param("authusername", "sun")
                .session(sess))
                .andExpect(model().attribute("message", "You have deactivated your account."))
                .andExpect((view().name("login")))
                .andExpect(status().isOk());
    }

}
