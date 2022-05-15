package nusiss.mini.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import nusiss.mini.model.User;

@Repository
public class UserRepo implements Queries{

    @Autowired
    private JdbcTemplate template;

    public int countUserByNameAndPassword(String username, String password) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_AND_COUNT_USER_BY_USERNAME_PASSWORD, username, password);
        if (!rs.next())
            return 0;
        return rs.getInt("user_count");
    }

    public int findUserByUsernameOrEmail(String username, String email) {
        final SqlRowSet rsUsername = template.queryForRowSet(SQL_SELECT_USER_BY_USERNAME, username);
        final SqlRowSet rsEmail = template.queryForRowSet(SQL_SELECT_USER_BY_EMAIL, email);
        if ((rsUsername.next()) || (rsEmail.next()))
            return 0;
        return 1;
    }

    public boolean insertUser(User user) {
        int count = template.update(SQL_INSERT_USER, 
                    user.getUsername(), user.getName(), user.getEmail(), user.getPassword());
        return 1 == count;
    }
}
