package nusiss.mini.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nusiss.mini.repository.Queries;
import nusiss.mini.repository.UserRepo;

@Service
public class UserService implements Queries{

    @Autowired
    private UserRepo usersRepo;

    public boolean authenticate(String username, String password) {
        return 1 == usersRepo.countUserByNameAndPassword(username, password);
    }

    public boolean filterRegistration(String username, String email) {
        return 1 == usersRepo.findUserByUsernameOrEmail(username, email);
    }
}
