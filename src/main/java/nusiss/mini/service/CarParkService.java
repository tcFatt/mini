package nusiss.mini.service;

import static nusiss.mini.model.ConvertUtil.*;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import nusiss.mini.model.CarPark;
import nusiss.mini.repository.CarParkRepo;
import nusiss.mini.repository.Queries;

@Service
public class CarParkService implements Queries {

    @Autowired
    private CarParkRepo cpRepo;

    private static final String API_URL = "https://api.data.gov.sg/v1/transport/carpark-availability";

    public Optional<CarPark> getCarParkInfo(String carParkNo) {       
        Optional<CarPark> opt = findCpInfoByCarParkNo(carParkNo);
        if (opt.isEmpty()) {
            return Optional.empty();
        }
        CarPark cp = opt.get();
        String map = "https://developers.onemap.sg/commonapi/staticmap/getStaticImage?layerchosen=default&lat="
                    +cp.getLatitude()+"&lng="+cp.getLongitude()+"&zoom=17&height=512&width=512";
        cp.setMap(map);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date now = new Date();
        String getCarParkUrl = UriComponentsBuilder
            .fromUriString(API_URL)
            .queryParam("date_time", sdf.format(now))
            .toUriString();
        RequestEntity<?> req = RequestEntity
            .get(getCarParkUrl)
            .accept(MediaType.APPLICATION_JSON)
            .build();
        RestTemplate template = new RestTemplate();    
        ResponseEntity<String> res = template.exchange(req, String.class);       
        JsonReader r = Json.createReader(new StringReader(res.getBody()));
        JsonObject o = r.readObject();
        JsonArray carParkArr = o.getJsonArray("items");
        for (int i = 0; i < carParkArr.size(); i++){
            JsonArray carParkDataArr = carParkArr
                .getJsonObject(i)
                .getJsonArray("carpark_data");
            for (int j = 0; j < carParkDataArr.size(); j++){
                JsonObject index = carParkDataArr.getJsonObject(j);
                if (carParkNo.equals(index.getString("carpark_number"))) {
                    JsonArray carParkInfoArr = index.getJsonArray("carpark_info");
                    Integer totalLots = 0;
                    Integer availableLots = 0;
                    for (int k = 0; k < carParkInfoArr.size(); k++){
                        Integer tempTotalLots = Integer.parseInt(carParkInfoArr.getJsonObject(k).getString("total_lots"));
                        Integer tempAvailableLots = Integer.parseInt(carParkInfoArr.getJsonObject(k).getString("lots_available"));
                        totalLots += tempTotalLots;
                        availableLots += tempAvailableLots;
                    }
                    cp.setAvailableLots(availableLots);
                    cp.setTotalLots(totalLots);
                }
            }
        }
        return Optional.of(cp);
    }

    public List<CarPark> findCpInfoByAddress(String searchAddress) {
        return convertCarParkAsList(cpRepo.queryCpInfoByAddress(searchAddress));
    }

    public Optional<CarPark> findCpInfoByCarParkNo(String carParkNo) {
        final SqlRowSet rs = cpRepo.queryCpInfoByCarParkNo(carParkNo);
        if (!rs.isBeforeFirst())
            return Optional.empty();
        return Optional.of(convertCarParkByRs(rs));
    }

    public List<CarPark> findFavouriteByUsername(String username) {
        List<CarPark> favouriteList = new LinkedList<>();
        final SqlRowSet rs = cpRepo.queryFavouriteByUsername(username);
        while (rs.next()) {
            CarPark cp = new CarPark();
            cp.setCarParkNo(rs.getString("car_park_no"));
            final SqlRowSet rs2 = cpRepo.queryCpInfoByCarParkNo(rs.getString("car_park_no"));
            while (rs2.next()) {
                cp.setAddress(rs2.getString("address"));
            }
            favouriteList.add(cp);
        }
        return favouriteList;
    }

    public boolean tallyFavouriteByUsernameAndCarParkNo(String username, String carParkNo) {
        return 1 == cpRepo.queryFavouriteByUsernameAndCarParkNo(username, carParkNo);
    }

    public void addFavourite(String username, String carParkNo) {
        cpRepo.insertFavourite(username, carParkNo);
    }

    public boolean removeFavourite(String username, String carParkNo) {
        return 1 == cpRepo.deleteFavourite(username,carParkNo);
    }

    public void removeFavourite(String username) {
        cpRepo.deleteFavourite(username);
    }

}