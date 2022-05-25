package nusiss.mini.repository;

import static nusiss.mini.model.ConvertUtil.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import nusiss.mini.model.User;

@Repository
public class UserRepo implements Queries {

    @Autowired
    private JdbcTemplate template;

    public int queryUserByNameAndPassword(String username, String password) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_USERNAME_AND_PASSWORD, username, password);
        if (!rs.next()) 
            return 0;
        return rs.getRow();
    }

    public int queryUserByUsernameOrEmail(String username, String email) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_USERNAME_OR_EMAIL, username, email);
        if (rs.next())
            return 0;
        return 1;
    }

    public void insertUser(User user) {
        template.update(SQL_INSERT_USER, 
            user.getUsername(), user.getName(), user.getEmail(), user.getPassword());
    }

    public User queryUserByUsername(String username) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_USERNAME, username);
        if (!rs.next()) 
            return null;
        return convertUserByRs(rs);
    }
    
    public int updateNameAndEmail(User user) {
        int count = template.update(SQL_UPDATE_USER_NAME_AND_EMAIL,
            user.getName(),user.getEmail(),user.getUsername());
        return count;
    }

    public int updatePassword(User user) {
        int count = template.update(SQL_UPDATE_USER_PASSWORD,
            user.getNewPassword(), user.getUsername(), user.getPassword());
        return count;
    }

    public int updatePasswordByUsernameAndEmail(User user) {
        int count = template.update(SQL_UPDATE_USER_PASSWORD_BY_USERNAME_AND_EMAIL,
            user.getNewPassword(), user.getUsername(), user.getEmail());
        return count;
    }
    
    public void deleteUser(String username) {
        template.update(SQL_DELETE_USER_BY_USERNAME, username);
    }
}

