package nusiss.mini.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class CarParkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeAll
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetFavouritesCarPark() throws Exception {
        mockMvc.perform(get("/favourite"))
                .andExpect(view().name("redirect:/secure/favourite"));
    }

    @Test
    void testGetHome() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(view().name("redirect:/secure/home"));
    }

    @Test
    void testPostAddFavourite() throws Exception {
        String carParkNo = "MM3";
        mockMvc.perform(post("/save")
                .param("car_park_no", carParkNo)
                .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(view().name("redirect:/secure/save"));
    }

    @Test
    void testPostRemoveFavourite() throws Exception {
        String carParkNo = "MM3";
        mockMvc.perform(post("/delete")
                .param("car_park_no", carParkNo)
                .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(view().name("redirect:/secure/delete"));
    }

    @Test
    void testPostSearchLocation() throws Exception {
        String location = "jurong";
        mockMvc.perform(post("/search")
                .param("location", location)
                .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(view().name("redirect:/secure/search"));
    }

    @Test
    void testPostSelectedCarPark() throws Exception {
        String carParkNo = "MM3";
        mockMvc.perform(post("/select")
                .param("car_park_no", carParkNo)
                .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(view().name("redirect:/secure/select"));
    }
}
