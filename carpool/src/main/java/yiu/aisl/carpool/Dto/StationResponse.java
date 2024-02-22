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

    public int getStationNum() {
        return stationNum;
    }

    public void setStationNum(int stationNum) {
        this.stationNum = stationNum;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getExitNum() {
        return exitNum;
    }

    public void setExitNum(int exitNum) {
        this.exitNum = exitNum;
    }
}