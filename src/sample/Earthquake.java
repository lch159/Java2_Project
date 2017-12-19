package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by 11611423 李晨昊 on 2017/12/7.16:19
 */
public class Earthquake {


    private StringProperty id;
    private StringProperty magnitude;
    private StringProperty UTC_date;
    private StringProperty latitude;
    private StringProperty longitude;
    private StringProperty depth;
    private StringProperty region;
    private StringProperty area_id;

    public Earthquake(StringEarthquake earthquake) {
        this.id = new SimpleStringProperty(earthquake.getID());
        this.magnitude = new SimpleStringProperty(earthquake.getMagnitude());
        this.UTC_date = new SimpleStringProperty(earthquake.getUTC_date());
        this.latitude = new SimpleStringProperty(earthquake.getLatitude());
        this.longitude = new SimpleStringProperty(earthquake.getLongitude());
        this.depth = new SimpleStringProperty(earthquake.getDepth());
        this.region = new SimpleStringProperty(earthquake.getRegion());

        this.area_id =new SimpleStringProperty(earthquake.getArea_id());
    }


    public void setID(String id) {
        this.id.set(id);
    }

    public String getID() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setMagnitude(String magnitude) {
        this.magnitude.set(magnitude);
    }

    public String getMagnitude() {
        return magnitude.get();
    }

    public StringProperty magnitudeProperty() {
        return magnitude;
    }

    public void setUTC_date(String UTC_date) {
        this.UTC_date.set(UTC_date);
    }

    public String getUTC_date() {
        return UTC_date.get();
    }

    public StringProperty UTC_dateProperty() {
        return UTC_date;
    }

    public void setLatitude(String latitude) {
        this.latitude.set(latitude);
    }

    public String getLatitude() {
        return latitude.get();
    }

    public StringProperty latitudeProperty() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude.set(longitude);
    }

    public String getLongitude() {
        return longitude.get();
    }

    public StringProperty longitudeProperty() {
        return longitude;
    }

    public void setDepth(String depth) {
        this.depth.set(depth);
    }

    public String getDepth() {
        return depth.get();
    }

    public StringProperty depthProperty() {
        return depth;
    }

    public void setRegion(String region) {
        this.region.set(region);
    }

    public String getRegion() {
        return region.get();
    }

    public StringProperty regionProperty() {
        return region;
    }

    public String getArea_id() {
        return area_id.get();
    }

    public StringProperty area_idProperty() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id.set(area_id);
    }
}
