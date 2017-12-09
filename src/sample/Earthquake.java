package sample;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by 11611423 李晨昊 on 2017/12/7.16:19
 */
public class Earthquake {
    private CsvReader earthquakeFile;
    private ArrayList<String[]> table;

    Earthquake(String path) {
        try {
            table = new ArrayList<>();
            earthquakeFile = new CsvReader(path);
            earthquakeFile.readHeaders();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String[]> getTable(String start_year, String end_year, String start_month, String end_month, String start_day, String end_day, String start_latitude, String end_latitude, String start_longitude, String end_longitude
            , String start_depth, String end_depth, String start_magnitude, String end_magnitude) {

        try {
            while (earthquakeFile.readRecord()) {

                String[] tempDate = earthquakeFile.get("UTC_date").split("[ -]");
                if (isFitValue(tempDate[0], start_year, end_year) && isFitValue(tempDate[1], start_month, end_month) && isFitValue(tempDate[2], start_day, end_day)&&isFitValue(earthquakeFile.get("latitude"),start_latitude,end_latitude)
                        &&isFitValue(earthquakeFile.get("longitude"),start_longitude,end_longitude)&&isFitValue(earthquakeFile.get("depth"),start_depth,end_depth)&&isFitValue(earthquakeFile.get("magnitude"),start_magnitude,end_magnitude)){
                    table.add(earthquakeFile.getValues());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return table;
    }

    private boolean isFitValue(String value, String start, String end) {
        return Integer.parseInt(value) >= Integer.parseInt(start) && Integer.parseInt(value) <= Integer.parseInt(end);

    }
}
