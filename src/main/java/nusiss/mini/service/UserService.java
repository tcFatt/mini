package nusiss.mini.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nusiss.mini.model.User;
import nusiss.mini.repository.Queries;
import nusiss.mini.repository.UserRepo;

@Service
public class UserService implements Queries{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CarParkService cpSvc;

    public boolean authenticateLogin(String username, String password) {
        return 1 == userRepo.queryUserByNameAndPassword(username, password);
    }

    public boolean checkExistingUserByUsernameOrEmail(String username, String email) {
        return 1 == userRepo.queryUserByUsernameOrEmail(username, email);
    }

    public void addNewUser(User user) {
        userRepo.insertUser(user);
    }

    public boolean changeUserNameAndEmail(User user) { 
        return 1 == userRepo.updateNameAndEmail(user);
    }

    public void deactivateUserAccount(String username) {
        while (!cpSvc.findFavouriteByUsername(username).isEmpty()) {
            cpSvc.removeFavourite(username);
        }
        userRepo.deleteUser(username);
    }

    public boolean changeUserPassword(User user) { 
        return 1 == userRepo.updatePassword(user);
    }

    public boolean resetUserPassword(User user) { 
        return 1 == userRepo.updatePasswordByUsernameAndEmail(user);
    }

    public Optional<User> findUserByUsername(String username) {
        User user = userRepo.queryUserByUsername(username);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

}