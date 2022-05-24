package nusiss.mini.model;

import java.util.LinkedList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.util.MultiValueMap;

public interface ConvertUtil {
    
    public static User convertUserByForm(MultiValueMap<String, String> form) {
        User u = new User();
        u.setUsername(form.getFirst("username"));
        u.setName(form.getFirst("name"));
        u.setEmail(form.getFirst("email"));
        u.setPassword(form.getFirst("password"));
        u.setNewPassword(form.getFirst("newPassword"));
        return u;
    }

    public static User convertUserByRs(SqlRowSet rs) {
        User u = new User();
        u.setUsername(rs.getString("username"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));        
        return u;
    }

    public static CarPark convertCarParkByRs(SqlRowSet rs) {
        CarPark cp = new CarPark();
        while (rs.next()) {
            cp.setCarParkNo(rs.getString("car_park_no"));
            cp.setLatitude(rs.getString("latitude"));
            cp.setLongitude(rs.getString("longitude"));
            cp.setAddress(rs.getString("address"));
            cp.setCarParkType(rs.getString("car_park_type"));
            cp.setParkingSystem(rs.getString("type_of_parking_system"));
            cp.setParkingTerm(rs.getString("short_term_parking"));
            cp.setFreeParking(rs.getString("free_parking"));
            cp.setNightParking(rs.getString("night_parking"));
            cp.setGantryHeight(rs.getString("gantry_height"));
            cp.setDecks(rs.getString("car_park_decks"));
        }
        return cp;
    }

    public static List<CarPark> convertCarParkAsList(SqlRowSet rs) {
        List<CarPark> cpList = new LinkedList<>();
        while (rs.next()) {
            CarPark cp = new CarPark();
            cp.setAddress(rs.getString("address"));
            cp.setCarParkNo(rs.getString("car_park_no"));
            cpList.add(cp);
        }
        return cpList;
    }

}
