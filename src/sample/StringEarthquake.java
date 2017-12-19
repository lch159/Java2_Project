package sample;

/**
 * Created by 11611423 李晨昊 on 2017/12/18.19:20
 */
public class StringEarthquake {
    private String id;
    private String magnitude;
    private String UTC_date;
    private String latitude;
    private String longitude;
    private String depth;
    private String region;
    private String area_id;


    public void setID(String id) {
        this.id = id;
    }

    public void setMagnitude(String magnitude) {
        this.magnitude = magnitude;
    }

    public void setUTC_date(String UTC_date) {
        this.UTC_date = UTC_date;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getID() {
        return this.id;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public String getUTC_date() {
        return UTC_date;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getDepth() {
        return depth;
    }

    public String getRegion() {
        return region;
    }

    public String getArea_id() {
        return area_id;
    }


}
