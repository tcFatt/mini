package nusiss.mini.service;

import static nusiss.mini.model.ConvertUtil.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import nusiss.mini.model.CarPark;
import nusiss.mini.model.User;
import nusiss.mini.repository.UserRepo;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CarParkServiceTest {

    @Autowired
    private CarParkService cpSvc;

    @Autowired
    private UserRepo userRepo;

    @AfterAll
    public void cleanup() {
        userRepo.deleteUser("temp");
    }

    @Order(1)
    @Test
    void testAddFavourite() {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("username", "temp");
        param.add("name", "Temp");
        param.add("email", "temp@gmail.com");
        param.add("password", "temp");
        User user = convertUserByForm(param);
        userRepo.insertUser(user);
        cpSvc.addFavourite("temp", "MM3");
        List<CarPark> cpList = cpSvc.findFavouriteByUsername("temp");
        assertTrue(!cpList.isEmpty());
    }

    @Order(2)
    @Test
    void testFindCpInfoByAddressShouldPass() {
        List<CarPark> cpList = cpSvc.findCpInfoByAddress("bishan");
        assertTrue(!cpList.isEmpty());
    }

    @Order(3)
    @Test
    void testFindCpInfoByAddressShouldFail() {
        List<CarPark> cpList = cpSvc.findCpInfoByAddress("unknoown");
        assertTrue(cpList.isEmpty());
    }

    @Order(4)
    @Test
    void testFindCpInfoByCarParkNoShouldPass() {
        Optional<CarPark> opt = cpSvc.findCpInfoByCarParkNo("MM3");
        assertTrue(opt.isPresent());
    }

    @Order(5)
    @Test
    void testFindCpInfoByCarParkNoShouldFail() {
        Optional<CarPark> opt = cpSvc.findCpInfoByCarParkNo("MM3MM3");
        assertFalse(opt.isPresent());
    }

    @Order(6)
    @Test
    void testFindFavouriteByUsername() {
        List<CarPark> cpList = cpSvc.findFavouriteByUsername("temp");
        assertTrue(!cpList.isEmpty());

    }

    @Order(7)
    @Test
    void testGetCarParkInfoShouldPass() {
        Optional<CarPark> opt = cpSvc.getCarParkInfo("MM3");
        assertTrue(opt.isPresent());
    }

    @Order(8)
    @Test
    void testGetCarParkInfoShouldFail() {
        Optional<CarPark> opt = cpSvc.getCarParkInfo("MM3MM3");
        assertFalse(opt.isPresent());
    }

    @Order(9)
    @Test
    void testTallyFavouriteByUsernameAndCarParkNoShouldPass() {
        assertTrue(cpSvc.tallyFavouriteByUsernameAndCarParkNo("temp", "MM3"));
    }

    @Order(10)
    @Test
    void testTallyFavouriteByUsernameAndCarParkNoShouldFail() {
        assertFalse(cpSvc.tallyFavouriteByUsernameAndCarParkNo("temp", "MM3MM3"));
    }

    @Order(11)
    @Test
    void testRemoveFavouriteByUsernameAndCarParkNoShouldPass() {
        assertTrue(cpSvc.removeFavourite("temp", "MM3"));
    }

    @Order(12)
    @Test
    void testRemoveFavouriteByUsernameAndCarParkNoShouldFail() {
        assertFalse(cpSvc.removeFavourite("temp", "MM3"));
    }

}
