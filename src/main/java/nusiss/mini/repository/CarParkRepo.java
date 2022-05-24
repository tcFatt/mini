package nusiss.mini.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;


@Repository
public class CarParkRepo implements Queries{

    @Autowired
    private JdbcTemplate template;

    public SqlRowSet queryCpInfoByAddress(String searchAddress) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_CP_INFO_BY_ADDRESS, "%" + searchAddress + "%");
        return rs;
    }

    public SqlRowSet queryCpInfoByCarParkNo(String carParkNo) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_CP_INFO_BY_CARPARKNO, carParkNo);     
        return rs;
    }

    public SqlRowSet queryFavouriteByUsername(String username) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_FAVOURITE_BY_USERNAME, username);     
        return rs;
    }

    public int queryFavouriteByUsernameAndCarParkNo(String username, String carParkNo) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_FAVOURITE_BY_USERNAME_CARPARKNO, username, carParkNo);
        if (!rs.next())
            return 0;
        return rs.getRow();
    }

    public void insertFavourite(String username, String carParkNo) {
        template.update(SQL_INSERT_FAVOURITE, username, carParkNo);
    }

    public int deleteFavourite(String username, String carParkNo) {
        int count = template.update(SQL_DELETE_FAVOURITE_BY_USERNAME_AND_CARPARKNO, username, carParkNo);
        return count;
    }

    public void deleteFavourite(String username) {
        template.update(SQL_DELETE_FAVOURITE_BY_USERNAME, username);
    }
     
}
