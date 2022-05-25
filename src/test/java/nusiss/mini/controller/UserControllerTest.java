package nusiss.mini.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import nusiss.mini.repository.Queries;
import nusiss.mini.repository.UserRepo;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest implements Queries{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockHttpSession sess;

    @Autowired
    private UserRepo userRepo;

    private MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

    private MultiValueMap<String, String> fake = new LinkedMultiValueMap<>();

    @BeforeAll
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        sess = new MockHttpSession();
        sess.setAttribute("username", "david");
        param.add("username", "david");
        param.add("name", "David");
        param.add("email", "david@gmail.com");
        param.add("password", "david");
        param.add("newPassword", "david");
        fake.add("username", "fake");
        fake.add("email", "fake@gmail.com");
        fake.add("newPassword", "fake");
    }

    @AfterAll
    public void cleanup() {
        userRepo.deleteUser("david");
    }

    @Test
    void testChangePassword() throws Exception {
        mockMvc.perform(post("/change")
                .params(param)
                .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect((view().name("redirect:/secure/change")))
                .andExpect(status().isFound());
    }

    @Test
    void testDeactivateUser() throws Exception {
        mockMvc.perform(post("/deactivate")
                .params(param)
                .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect((view().name("redirect:/secure/deactivate")))
                .andExpect(status().isFound());
    }

    @Test
    void testEditProfile() throws Exception {
        mockMvc.perform(post("/edit")
                .params(param)
                .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect((view().name("redirect:/secure/edit")))
                .andExpect(status().isFound());
    }

    @Test
    void testGetLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(view().name("login"));
    }

    @Test
    void testGetLogout() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(model().attribute("message", "You have logged out successfully."))
                .andExpect(view().name("login"));
    }

    @Test
    void testGetProfile() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(view().name("redirect:/secure/profile"));
    }

    @Test
    void testGetRegistration() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(view().name("registration"));
    }

    @Test
    void testGetReset() throws Exception {
        mockMvc.perform(get("/reset"))
                .andExpect(view().name("reset"));
    }

    @Order(1)
    @Test
    void testPostRegistrationShouldPass() throws Exception {
        String message2 = "You have registered successfully.";
        mockMvc.perform(post("/register")
                .params(param)
                .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(model().attribute("message2", message2))
                .andExpect(status().isOk());
    }

    @Order(2)
    @Test
    void testPostRegistrationShouldFail() throws Exception {
        String message1 = "Username or email is exists. Please try again.";
        mockMvc.perform(post("/register")
                .params(param) // using same param to register will fail, user exists.
                .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(model().attribute("message1", message1))
                .andExpect(status().isBadRequest());
    }

    @Order(3)
    @Test
    void testPostAuthenticateShouldPass() throws Exception {
        mockMvc.perform(post("/login")
                .params(param)
                .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect((view().name("redirect:/secure/home")))
                .andExpect(status().isFound());
    }

    @Order(4)
    @Test
    void testPostAuthenticateShouldFail() throws Exception {
        String message = "Invalid username or password! Please try again.";
        mockMvc.perform(post("/login")
                .params(fake)
                .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(model().attribute("message", message))
                .andExpect((view().name("login")))
                .andExpect(status().isBadRequest());
    }

    @Order(5)
    @Test
    void testResetPasswordShouldPass() throws Exception {
        String message2 = "You have reset your password. Please log in with the new password.";
        mockMvc.perform(post("/reset")
                .params(param)
                .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(model().attribute("message2", message2))
                .andExpect(status().isOk());
    }

    @Order(6)
    @Test
    void testResetPasswordShouldFail() throws Exception {
        String message1 = "Invalid username or email! Please try again.";
        mockMvc.perform(post("/reset")
                .params(fake)
                .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(model().attribute("message1", message1))
                .andExpect(status().isBadRequest());
    }

}
