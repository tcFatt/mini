package nusiss.mini.service;

import static nusiss.mini.model.ConvertUtil.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import nusiss.mini.model.User;
import nusiss.mini.repository.UserRepo;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private UserService userSvc;

    private static final MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

    @BeforeEach
    public void setup() {
        param.add("username", "moon");
        param.add("name", "Moon");
        param.add("email", "moon@gmail.com");
        param.add("password", "moon");
    }

    @AfterEach
    public void cleanup() {
        userRepo.deleteUser("moon");
    }

    @Test
    void testAddNewUser() {
        User user = convertUserByForm(param);
        userSvc.addNewUser(user);
        Optional<User> opt = userSvc.findUserByUsername(user.getUsername());
        assertFalse(opt.isEmpty());
    }

    @Test
    void testAuthenticateLoginShouldPass() {
        User user = convertUserByForm(param);
        userSvc.addNewUser(user);
        assertTrue(userSvc.authenticateLogin("moon", "moon"));   
    }

    @Test
    void testAuthenticateLoginShouldFail() {
        User user = convertUserByForm(param);
        userSvc.addNewUser(user);
        assertFalse(userSvc.authenticateLogin("moon", "wrong"));   
    }

    @Test
    void testChangeUserNameAndEmailShouldPass()  {
        User user = convertUserByForm(param);
        userSvc.addNewUser(user);
        user.setName("newmoon");
        user.setEmail("newmoon@gmail.com");
        assertTrue(1 == userRepo.updateNameAndEmail(user));
        String newName = user.getName();
        String newEmail = user.getEmail();
        assertTrue(!userSvc.checkExistingUserByUsernameOrEmail(newName, newEmail));
    }

    @Test
    void testChangeUserPassword() {
        User user = convertUserByForm(param);
        userSvc.addNewUser(user);
        user.setNewPassword("newmoon");
        assertTrue(userSvc.resetUserPassword(user));
    }

    @Test
    void testCheckExistingUserByUsernameOrEmail() {
        User user = convertUserByForm(param);
        userSvc.addNewUser(user);
        assertTrue(!userSvc.checkExistingUserByUsernameOrEmail("moon", "moon@gmail.com")); // 0 is exists.
    }

    @Test
    void testDeactivateUserAccountShouldPass() {
        User user = convertUserByForm(param);
        userSvc.addNewUser(user);
        userRepo.deleteUser("moon");
        Optional<User> opt = userSvc.findUserByUsername("moon");
        assertTrue(opt.isEmpty());
    }

    @Test
    void testFindUserByUsername() {
        User user = convertUserByForm(param);
        userSvc.addNewUser(user);
        Optional<User> opt = userSvc.findUserByUsername(user.getUsername());
        assertTrue(opt.isPresent());
    }

    @Test
    void testResetUserPassword() {
        User user = convertUserByForm(param);
        userSvc.addNewUser(user);
        user.setNewPassword("newmoon");
        assertTrue(userSvc.resetUserPassword(user));
    }
}
