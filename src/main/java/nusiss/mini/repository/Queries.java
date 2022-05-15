package nusiss.mini.repository;

public interface Queries {

    public static final String SQL_INSERT_USER = 
        "insert into user (username, name, email, password) values (?, ?, ?, sha1(?))";

    public static final String SQL_SELECT_AND_COUNT_USER_BY_USERNAME_PASSWORD =
        "select count(*) as user_count from user where username = ? and password = sha1(?)";

    public static final String SQL_SELECT_USER_BY_USERNAME = 
        "select * from user where username = ?";

    public static final String SQL_SELECT_USER_BY_EMAIL = 
        "select * from user where email = ?";


   
}
