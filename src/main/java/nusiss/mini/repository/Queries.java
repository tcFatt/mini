package nusiss.mini.repository;

public interface Queries {
    // user
    public static final String SQL_INSERT_USER = 
        "insert into user (username, name, email, password) values (?, ?, ?, sha1(?))";

    public static final String SQL_SELECT_USER_BY_USERNAME_AND_PASSWORD =
        "select * from user where username = ? and password = sha1(?)";

    public static final String SQL_SELECT_USER_BY_USERNAME_OR_EMAIL = 
        "select * from user where username = ? or email = ?";

    public static final String SQL_SELECT_USER_BY_USERNAME = 
        "select * from user where username = ?";

    public static final String SQL_DELETE_USER_BY_USERNAME = 
        "delete from user where username = ?";

    public static final String SQL_UPDATE_USER_NAME_AND_EMAIL =
        "update user set name = ?, email = ? where username = ?";

    public static final String SQL_UPDATE_USER_PASSWORD =
        "update user set password = sha1(?) where username = ? and password = sha1(?)";

    public static final String SQL_UPDATE_USER_PASSWORD_BY_USERNAME_AND_EMAIL =
        "update user set password = sha1(?) where username = ? and email = ?";

    // cp_info
    public static final String SQL_SELECT_CP_INFO_BY_CARPARKNO = 
        "select * from cp_info where car_park_no = ?";

    public static final String SQL_SELECT_CP_INFO_BY_ADDRESS = 
        "select * from cp_info where address like ?";

    // favourite
    public static final String SQL_SELECT_FAVOURITE_BY_USERNAME = 
        "select * from favourite where username = ?";

    public static final String SQL_DELETE_FAVOURITE_BY_USERNAME_AND_CARPARKNO = 
        "delete from favourite where username = ? and car_park_no = ?";

    public static final String SQL_DELETE_FAVOURITE_BY_USERNAME = 
        "delete from favourite where username = ?";

    public static final String SQL_INSERT_FAVOURITE =
        "insert into favourite (username, car_park_no) values (?, ?)";

    public static final String SQL_SELECT_FAVOURITE_BY_USERNAME_CARPARKNO =
        "select * from favourite where username = ? and car_park_no = ?";
   
}
