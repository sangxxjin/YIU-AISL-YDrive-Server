package yiu.aisl.carpool.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yiu.aisl.carpool.domain.Station;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StationResponse {
    private int stationNum;
    private String stationName;
    private String city;
    private String district;
    private int exitNum;

    public StationResponse(Station station) {
        this.city = station.getCity();
        this.district = station.getDistrict();
        this.stationNum = station.getStationNum();
        this.exitNum = station.getExitNum();
        this.stationName = station.getStationName();
    }

    public StationResponse(String city) {
        this.city = city;
    }

}