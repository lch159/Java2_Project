package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This class is the core of this application. <br>
 * This class implements GUI and functions of this application
 *
 * @author lichenhao
 * @author linsen
 * @author zhengxiaodian
 * @version 1.00
 */
public class Controller_SeniorQuery implements Initializable {

    @FXML
    Button button_query;
    @FXML
    DatePicker datePicker_start;
    @FXML
    DatePicker datePicker_end;
    @FXML
    TextField textField_latitude_start;
    @FXML
    TextField textField_latitude_end;
    @FXML
    TextField textField_longitude_start;
    @FXML
    TextField textField_longitude_end;
    @FXML
    TextField textField_depth_start;
    @FXML
    TextField textField_depth_end;
    @FXML
    TextField textField_magnitude_start;
    @FXML
    TextField textField_magnitude_end;
    @FXML
    TableView<Earthquake> tableView_table;
    @FXML
    TableColumn<Earthquake, String> tableColumn_ID;
    @FXML
    TableColumn<Earthquake, String> tableColumn_magnitude;
    @FXML
    TableColumn<Earthquake, String> tableColumn_Date;
    @FXML
    TableColumn<Earthquake, String> tableColumn_latitude;
    @FXML
    TableColumn<Earthquake, String> tableColumn_longitude;
    @FXML
    TableColumn<Earthquake, String> tableColumn_depth;
    @FXML
    TableColumn<Earthquake, String> tableColumn_region;
    @FXML
    TableColumn<Earthquake, String> tableColumn_area_id;
    @FXML
    RadioButton radioButton_fromCSV;
    @FXML
    RadioButton radioButton_fromDB;
    @FXML
    RadioButton radioButton_fromWeb;
    @FXML
    TextField textField_browse;
    @FXML
    Button button_browse;
    @FXML
    ImageView imageView_map;
    @FXML
    Pane pane_image;
    @FXML
    ComboBox comboBox_area1;
    @FXML
    ComboBox comboBox_area2;
    @FXML
    RadioButton radioButton_region;
    @FXML
    RadioButton radioButton_plate;
    @FXML
    ComboBox comboBox_region;
    @FXML
    Label label_region;
    @FXML
    Label label_plate1;
    @FXML
    Label label_plate2;
    @FXML
    RadioButton radioButton_null;
    @FXML
    RadioButton radioButton_fiftyMinutes;
    @FXML
    RadioButton radioButton_day;
    @FXML
    Label label_state;
    @FXML
    MenuItem menuItem_scrapping;
    @FXML
    MenuItem menuItem_openFile;
    @FXML
    MenuItem menuItem_exit;
    @FXML
    CategoryAxis barChart_xAxis;
    @FXML
    NumberAxis barChart_yAxis;
    @FXML
    BarChart barChart_bc;


    private static Double start_year;
    private static Double start_month;
    private static Double start_day;
    private static Double end_year;
    private static Double end_month;
    private static Double end_day;
    private static Double start_latitude;
    private static Double end_latitude;
    private static Double start_longitude;
    private static Double end_longitude;
    private static Double start_depth;
    private static Double end_depth;
    private static Double start_magnitude;
    private static Double end_magnitude;
    private ObservableList<Earthquake> data = FXCollections.observableArrayList();//data related to tableview
    private ObservableList<String> magnitudeData = FXCollections.observableArrayList();
    private static ArrayList<String[]> table = new ArrayList<>();//Store searched data
    private static String fileType;//select file type
    private static List<String> filePostFix;//select file postfix
    private static Circle mark;//mark on map
    private boolean isChangedFile = false;//whether file source changed
    private Map<String, String[]> plates = new HashMap<>();
    private Map<String, String> areas = new HashMap<>();
    private static Set<String> regions = new HashSet<>();
    private Map<Integer, LinkedList<String>> numOfMagnitude = new TreeMap<>();


    /**
     * To judge whether the textfield is empty
     * @param textField
     * @return true when the textfield is empty
     */
    private boolean isContentEmpty(TextField textField) {
        return textField.getText().isEmpty();
    }
    /**
     * To judge whether the textfield is empty
     * @param datePicker
     * @return true when the textfield is empty
     */
    private boolean isContentEmpty(DatePicker datePicker) {
        return datePicker.getEditor().getText().isEmpty();
    }

    /**
     * To acquire the starting date
     * @return the start date entered by the user or 2017-01-01 when nothing entered
     */
    private String getStart_Date() {
        return isContentEmpty(datePicker_start) ? "2017-01-01" : datePicker_start.getEditor().getText().trim();
    }

    /**
     * To acquire the end date
     * @return the end date entered by the user or 2017-12-31 when nothing entered
     */
    private String getEnd_Date() {

        return isContentEmpty(datePicker_end) ? "2017-12-31" : datePicker_end.getEditor().getText().trim();
    }

    /**
     * To acquire the starting latitude
     * @return the starting latitude entered by the user or -90 when nothing entered
     */
    private Double getStart_latitude() {
        return isContentEmpty(textField_latitude_start) ? -90 : Double.parseDouble(textField_latitude_start.getText().trim());
    }
    /**
     * To acquire the end latitude
     * @return the end latitude entered by the user or 90 when nothing entered
     */
    private Double getEnd_latitude() {
        return isContentEmpty(textField_latitude_end) ? 90 : Double.parseDouble(textField_latitude_end.getText().trim());
    }
    /**
     * To acquire the starting longitude
     * @return the starting longitude entered by the user or -180 when nothing entered
     */
    private Double getStart_longitude() {
        return isContentEmpty(textField_longitude_start) ? -180 : Double.parseDouble(textField_longitude_start.getText().trim());
    }
    /**
     * To acquire the end longitude
     * @return the end longitude entered by the user or 180 when nothing entered
     */
    private Double getEnd_longitude() {
        return isContentEmpty(textField_longitude_end) ? 180 : Double.parseDouble(textField_longitude_end.getText().trim());
    }
    /**
     * To acquire the starting depth
     * @return the starting depth entered by the user or -1000 when nothing entered
     */
    private Double getStart_depth() {
        return isContentEmpty(textField_depth_start) ? -1000 : Double.parseDouble(textField_depth_start.getText().trim());
    }
    /**
     * To acquire the end depth
     * @return the end depth entered by the user or 1000 when nothing entered
     */
    private Double getEnd_depth() {
        return isContentEmpty(textField_depth_end) ? 1000 : Double.parseDouble(textField_depth_end.getText().trim());
    }
    /**
     * To acquire the starting intensity
     * @return the starting intensity entered by the user or 0 when nothing entered
     */
    private Double getStart_magnitude() {
        return isContentEmpty(textField_magnitude_start) ? 0 : Double.parseDouble(textField_magnitude_start.getText().trim());
    }
    /**
     * To acquire the end intensity
     * @return the end intensity entered by the user or 10 when nothing entered
     */
    private Double getEnd_magnitude() {
        return isContentEmpty(textField_magnitude_end) ? 10 : Double.parseDouble(textField_magnitude_end.getText().trim());
    }

    /**
     * To acquire the first plate
     * @return the first plate chosen by the user or 10 when nothing entered
     */
    private String getPlate1() {
        return String.valueOf(comboBox_area1.getValue());
    }
    /**
     * To acquire the second plate
     * @return the second plate chosen by the user or 10 when nothing entered
     */
    private String getPlate2() {
        return String.valueOf(comboBox_area2.getValue());
    }


    private String getRegion() {
        return String.valueOf(comboBox_region.getValue());
    }
    /**
     * To set starting date
     * @param date starting date entered by user
     */
    private void setStartDate(String date) {
        String[] startDate = date.split("-");
        start_year = Double.parseDouble(startDate[0]);
        start_month = Double.parseDouble(startDate[1]);
        start_day = Double.parseDouble(startDate[2]);
    }
    /**
     * To set end date
     * @param date end date entered by user
     */
    private void setEndDate(String date) {
        String[] endDate = date.split("-");
        end_year = Double.parseDouble(endDate[0]);
        end_month = Double.parseDouble(endDate[1]);
        end_day = Double.parseDouble(endDate[2]);
    }


    /**
     * To judge whether each data is in the range query
     * @param value the current value to be tested
     * @param start lower bound
     * @param end upper bound
     * @return true when the current value is between the lower bound and the upper bound
     */
    private boolean isFitValue(Double value, Double start, Double end) {
        return value >= start && value <= end;
    }

    /**
     * To judge whether each data is in the chosen area or plate
     * @param area_id the id of each earthquake
     * @param region the region where earthquake happened
     * @return true when the earthquake is in the chosen area or plate
     */
    private boolean isSelectedArea(String region, String area_id) {

        return radioButton_region.isSelected() && (getRegion().equals("All") || getRegion().equals(region)) || radioButton_plate.isSelected() && ((areas.get(getPlate1()).equals("All") && areas.get(getPlate1()).equals("All")) || (areas.get(getPlate2()).equals("All") && plates.get(area_id)[0].equals(areas.get(getPlate1())))
                || (areas.get(getPlate1()).equals("All") && plates.get(area_id)[1].equals(areas.get(getPlate2()))) || (plates.get(area_id)[0].equals(areas.get(getPlate1())) && plates.get(area_id)[1].equals(areas.get(getPlate2()))));
    }

    /**
     * To judge whether each data is fit in all searching conditions
     * @param elem a string array with all data stored
     * @return true when the earthquake is fit in all searching conditions
     */
    private boolean isFitValues(String[] elem) {
        String[] tempDate = elem[1].split("[ -]");
        String[] tempTime = tempDate[3].split("[:]");
        boolean flag = false;

        if (radioButton_null.isSelected())
            flag = isFitValue(Double.parseDouble(tempDate[0]), start_year, end_year) && isFitValue(Double.parseDouble(tempDate[1]), start_month, end_month) && isFitValue(Double.parseDouble(tempDate[2]), start_day, end_day);
        else if (radioButton_fiftyMinutes.isSelected()) {
            Calendar calendar = Calendar.getInstance();

            calendar.add(Calendar.MINUTE, -50);
            int nyear = calendar.get(Calendar.YEAR);
            int nmonth = calendar.get(Calendar.MONTH) + 1;
            int ndate = calendar.get(Calendar.DATE);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            flag = Integer.parseInt(tempDate[0]) == nyear && Integer.parseInt(tempDate[1]) == nmonth && Integer.parseInt(tempDate[2]) == ndate && ((Integer.parseInt(tempTime[0]) == hour && Integer.parseInt(tempTime[1]) >= minute) || (Integer.parseInt(tempTime[0]) > hour));


        }
        if (radioButton_day.isSelected()) {
            Calendar calendar = Calendar.getInstance();

            calendar.add(Calendar.HOUR, -24);
            int nyear = calendar.get(Calendar.YEAR);
            int nmonth = calendar.get(Calendar.MONTH) + 1;
            int ndate = calendar.get(Calendar.DATE);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            flag = Integer.parseInt(tempDate[0]) == nyear && Integer.parseInt(tempDate[1]) == nmonth && (Integer.parseInt(tempDate[2]) > ndate || (Integer.parseInt(tempDate[2]) == ndate && ((Integer.parseInt(tempTime[0]) == hour && Integer.parseInt(tempTime[1]) >= minute) || (Integer.parseInt(tempTime[0]) > hour))));

        }


        return flag && isFitValue(Double.parseDouble(elem[2]), start_latitude, end_latitude)
                && isFitValue(Double.parseDouble(elem[3]), start_longitude, end_longitude) && isFitValue(Double.parseDouble(elem[4]), start_depth, end_depth) && isFitValue(Double.parseDouble(elem[5]), start_magnitude, end_magnitude) && isSelectedArea(elem[6], elem[7]);
    }

    /**
     * To acquire all data entered by user
     */
    private void getData() {
        setStartDate(getStart_Date());
        setEndDate(getEnd_Date());
        start_latitude = getStart_latitude();
        end_latitude = getEnd_latitude();
        start_longitude = getStart_longitude();
        end_longitude = getEnd_longitude();
        start_depth = getStart_depth();
        end_depth = getEnd_depth();
        start_magnitude = getStart_magnitude();
        end_magnitude = getEnd_magnitude();
    }

    /**
     * To acquire earthquake data from a csv file
     * @param path the path of the csv file
     */
    private void readDataFromCsv(String path) {
        boolean flag = true;
        try {
            if (table != null)
                table.clear();


                Class.forName("org.sqlite.JDBC");// Loading the driver and connect to sqlite
                Connection connection = DriverManager.getConnection("jdbc:sqlite:earthquakes-2.db");
                Statement clearData = connection.createStatement();
                clearData.execute(" delete from quakes");
                CsvReader earthquakeFile = new CsvReader(path);
                earthquakeFile.readHeaders();
                while (earthquakeFile.readRecord()) {

                String[] values = earthquakeFile.getValues();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO quakes(id,UTC_date,latitude,longitude,depth,magnitude,region) VALUES (?,?,?,?,?,?,?)");
                    preparedStatement.setInt(1, Integer.parseInt(values[0]));
                    preparedStatement.setString(2, values[1]);
                    preparedStatement.setDouble(3, Double.parseDouble(values[2]));
                    preparedStatement.setDouble(4, Double.parseDouble(values[3]));
                    preparedStatement.setInt(5, Integer.parseInt(values[4]));
                    preparedStatement.setDouble(6, Double.parseDouble(values[5]));
                    preparedStatement.setString(7, values[6]);
                    preparedStatement.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            connection.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * to acquire earthquake data from database
     * @param databaseName the name of the database
     * @param tableName the name of the table
     */
    private void readDataFromDatabase(String databaseName, String tableName) {
        try {
            if (table != null)
                table.clear();

                String drive = "org.sqlite.JDBC";
                Class.forName(drive);//Loading the driver and connect to sqlite
                Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
                Statement statement = connection.createStatement();
                ResultSet rSet = statement.executeQuery("select * from " + tableName);//Query database, put searched data in the resultset
                while (rSet.next()) {            //Traverse the resultset
                    String[] rowData = new String[8];
                    for (int i = 1; i <= 8; i++) {
                        rowData[i - 1] = rSet.getString(i);
                    }
                    regions.add(rowData[6]);
                    table.add(rowData);
                }
                rSet.close();//Close resultset
                connection.close();//close database connection
            comboBox_region.getItems().addAll(regions);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To acquire earthquake data by scraping from web page www.emsc-csem.org/Earthquake/
     * @param pages the website of the web page
     */
    private void scrapingDataFromWebsite(int pages) {
        Document document;
        try {
            Class.forName("org.sqlite.JDBC");//Loading the driver and connect to sqlite
            Connection connection = DriverManager.getConnection("jdbc:sqlite:earthquakes-2.db");
            Statement clearData = connection.createStatement();
            clearData.execute(" delete from quakes");

            Set<String> lastIDs =new HashSet<>();
            lastIDs.add("0");
            for (int i = 1; i <= pages; i++) {
                String url = String.valueOf(new StringBuilder("https://www.emsc-csem.org/Earthquake/?view=").append(i));
                document = Jsoup.connect(url).get();
                assert document != null;
                Elements element1 = document.getElementById("tbody").getElementsByTag("tr");

                Pattern pattern = Pattern.compile("[0-9]*");

                for (Element elem : element1) {
                    if (pattern.matcher(elem.id()).matches()) {
                        String[] rowData = new String[7];
                        rowData[0] = elem.id();
                        rowData[1] = elem.getElementsByClass("tabev6").get(0).getElementsByTag("a").get(0).text();
                        rowData[2] = elem.getElementsByClass("tabev2").get(0).text().equals("N") ? elem.getElementsByClass("tabev1").get(0).text() : String.valueOf(new StringBuilder("-").append(elem.getElementsByClass("tabev1").get(0).text()));
                        rowData[3] = elem.getElementsByClass("tabev2").get(1).text().equals("E") ? elem.getElementsByClass("tabev1").get(1).text() : String.valueOf(new StringBuilder("-").append(elem.getElementsByClass("tabev1").get(1).text()));
                        rowData[4] = elem.getElementsByClass("tabev3").get(0).text();
                        rowData[5] = elem.getElementsByClass("tabev2").get(2).text();
                        rowData[6] = elem.getElementsByClass("tb_region").get(0).text();

                        //Store the scraped data in the database
                        if (lastIDs.contains(elem.id()))
                        {
                            lastIDs.remove(elem.id());
                            continue;
                        }else {
                            lastIDs.add(elem.id());
                        }

                        try {
                            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO quakes(id,UTC_date,latitude,longitude,depth,magnitude,region) VALUES (?,?,?,?,?,?,?)");
                            preparedStatement.setInt(1, Integer.parseInt(rowData[0]));
                            preparedStatement.setString(2, rowData[1]);
                            preparedStatement.setDouble(3, Double.parseDouble(rowData[2]));
                            preparedStatement.setDouble(4, Double.parseDouble(rowData[3]));
                            preparedStatement.setInt(5, Integer.parseInt(rowData[4]));
                            preparedStatement.setDouble(6, Double.parseDouble(rowData[5]));
                            preparedStatement.setString(7, rowData[6]);
                            preparedStatement.execute();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }

                }

            }
            connection.close();//close database connection
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * To show the acquired data in a table
     */
    private void showTable() {
        if (!data.isEmpty())
        {
            data.clear();
            for (int i = 0; i < 5; i++) {
                numOfMagnitude.put(i,new LinkedList<>());
            }
        }

        for (String[] elem : table) {
            if (isFitValues(elem)) {
                StringEarthquake stringEarthquake = new StringEarthquake();
                stringEarthquake.setID(elem[0]);
                stringEarthquake.setUTC_date(elem[1]);
                stringEarthquake.setLatitude(elem[2]);
                stringEarthquake.setLongitude(elem[3]);
                stringEarthquake.setDepth(elem[4]);
                stringEarthquake.setMagnitude(elem[5]);
                stringEarthquake.setRegion(elem[6]);
                stringEarthquake.setArea_id(elem[7]);
                Earthquake earthquake = new Earthquake(stringEarthquake);
                data.add(earthquake);
                double magnitude = Double.parseDouble(elem[5]);
                if (magnitude >= 1 && magnitude < 3) {
                    numOfMagnitude.get(0).add(elem[0]);
                } else if (magnitude >= 3 && magnitude < 4.5) {
                    numOfMagnitude.get(1).add(elem[1]);
                } else if (magnitude >= 4.5 && magnitude < 6) {
                    numOfMagnitude.get(2).add(elem[1]);
                } else if (magnitude >= 6 && magnitude < 7) {
                    numOfMagnitude.get(3).add(elem[1]);
                } else if (magnitude >= 7) {
                    numOfMagnitude.get(4).add(elem[1]);
                }
            }
        }
        label_state.setText(table.size()+" earthquakes been queried");
        tableView_table.setItems(data);
    }

    /**
     * To show the acquired data in a map
     */
    private void showMap() {
        ImageView mapView = imageView_map;
        pane_image.getChildren().clear();
        pane_image.getChildren().add(mapView);
        double x = imageView_map.getFitWidth();
        double y = imageView_map.getFitHeight();
        imageView_map.getImage().getWidth();
        int size = data.size();

        double[][] tempData = new double[size + 1][2];
        int i = 0;

        for (Earthquake temp : data) {
            double longitude = Double.parseDouble(temp.getLongitude());
            double latitude = Double.parseDouble(temp.getLatitude());
            double xp;
            double yp;

            if (longitude >= 0) {
                xp = longitude / 360 * x;
            } else {
                xp = (360 + longitude) / 360 * x;
            }
            yp = (90 - latitude) / 180 * y;

            tempData[i][0] = xp;
            tempData[i][1] = yp;
            i++;
        }

        while (i >= 0) {
            mark = new Circle(tempData[i][0], tempData[i][1], 2);
            mark.setFill(Color.RED);
            pane_image.getChildren().add(mark);
            i--;
        }
    }

    /**
     * To show data in a bar chart
     */
    private void showBarChart() {
        if (!magnitudeData.isEmpty()) {
            magnitudeData.clear();
            barChart_bc.getData().clear();
        }
        XYChart.Series<String, Integer> series = new XYChart.Series();

        String[] magnitudes = {"[1,3)", "[3,4.5)", "[4.5,6)", "[6,7)", "[7,∞)"};

        magnitudeData.addAll(magnitudes);
        barChart_xAxis.setCategories(magnitudeData);
        series.setName("barChart");

        for (int i = 0; i < 5; i++) {
            series.getData().add(new XYChart.Data<>(magnitudeData.get(i), numOfMagnitude.get(i).size()));
        }

        barChart_bc.getData().add(series);
    }
    /**
     * Configuration of radio buttons
     */
    private void radioButtonConfig() {
        radioButton_fromCSV.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                textField_browse.clear();
        });

        radioButton_fromDB.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                textField_browse.clear();
        });
        radioButton_fromWeb.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                button_browse.setDisable(true);
                textField_browse.setText("https://www.emsc-csem.org/Earthquake");
                textField_browse.setDisable(true);
            } else {
                button_browse.setDisable(false);
                textField_browse.setDisable(false);
            }
        });
        textField_browse.textProperty().addListener((observable, oldValue, newValue) -> {
            isChangedFile = !newValue.equals(oldValue);
        });

        ToggleGroup toggleGroup = new ToggleGroup();
        radioButton_fromCSV.setToggleGroup(toggleGroup);
        radioButton_fromDB.setToggleGroup(toggleGroup);
        radioButton_fromWeb.setToggleGroup(toggleGroup);
        radioButton_fromCSV.setSelected(true);


        radioButton_region.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                label_region.setVisible(true);
                comboBox_region.setVisible(true);
                label_plate1.setVisible(false);
                label_plate2.setVisible(false);
                comboBox_area1.setVisible(false);
                comboBox_area2.setVisible(false);
            }
        });
        radioButton_plate.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                label_region.setVisible(false);
                comboBox_region.setVisible(false);
                label_plate1.setVisible(true);
                label_plate2.setVisible(true);
                comboBox_area1.setVisible(true);
                comboBox_area2.setVisible(true);
            } else {
                label_region.setVisible(false);
                comboBox_region.setVisible(false);
                label_plate1.setVisible(false);
                label_plate2.setVisible(false);
                comboBox_area1.setVisible(false);
                comboBox_area2.setVisible(false);
            }
        });

        ToggleGroup toggleGroup1 = new ToggleGroup();
        radioButton_region.setToggleGroup(toggleGroup1);
        radioButton_plate.setToggleGroup(toggleGroup1);
        radioButton_region.setSelected(true);

        radioButton_null.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    datePicker_start.setDisable(false);
                    datePicker_end.setDisable(false);
                }
            }
        });
        radioButton_fiftyMinutes.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    datePicker_start.setDisable(true);
                    datePicker_end.setDisable(true);
                }
            }
        });
        radioButton_day.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    datePicker_start.setDisable(true);
                    datePicker_end.setDisable(true);
                }
            }
        });
        radioButton_null.setSelected(true);
        ToggleGroup toggleGroup2 = new ToggleGroup();
        radioButton_null.setToggleGroup(toggleGroup2);
        radioButton_fiftyMinutes.setToggleGroup(toggleGroup2);
        radioButton_day.setToggleGroup(toggleGroup2);
    }

    /**
     * Configuration of combobox
     */
    private void comboBoxConfig() {


        try {
            Class.forName("org.sqlite.JDBC");//Loading the driver and connect to sqlite
            Connection connection = DriverManager.getConnection("jdbc:sqlite:earthquakes-2.db");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(" SELECT code,name FROM plates ");
            areas.put("All", "All");
            while (resultSet.next()) {
                areas.put(resultSet.getString("name"), resultSet.getString("code"));
            }
            ResultSet resultSet1 = statement.executeQuery("SELECT id , plate1 , plate2 FROM plate_areas");
            while (resultSet1.next()) {

                String[] tempPlates = {resultSet1.getString("plate1"), resultSet1.getString("plate2")};
                plates.put(resultSet1.getString("id"), tempPlates);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        regions.add("All");

        comboBox_region.setValue("All");
        comboBox_area1.setValue("All");
        comboBox_area1.getItems().addAll(areas.keySet());
        comboBox_area2.setValue("All");
        comboBox_area2.getItems().addAll(areas.keySet());

    }


    /**
     * Configuration of error information dialog
     * @param info The error information
     */
    private void errorInfoDialog(String info) {
        Alert warning = new Alert(Alert.AlertType.WARNING, info);
        Button warn = new Button();
        warn.setOnAction(e -> warning.showAndWait());
        warning.setTitle("Error information");
        warning.show();
    }

    /**
     * Different error conditions
     * @return true when error happened
     */
    private boolean isErrorCondition() {
        boolean flag = true;
        if (start_year > end_year || start_month > end_month || start_day > end_day)
            errorInfoDialog("The date is wrong or the lower bound is greater than the upper bound");
        else if (!isFitValue(start_latitude, -90.0, 90.0) || !isFitValue(end_latitude, -90.0, 90.0) || start_latitude > end_latitude)
            errorInfoDialog("The latitude is wrong or the lower bound is greater than the upper bound \n\nTips: -90 <= latitude <= 90");
        else if (!isFitValue(start_longitude, -180.0, 180.0) || !isFitValue(end_longitude, -180.0, 180.0) || start_longitude > end_longitude)
            errorInfoDialog("The longitude is wrong or the lower bound is greater than the upper bound \n\nTips: -180 <= longitude <= 180");
        else if (!isFitValue(start_depth, -1000.0, 1000.0) || !isFitValue(start_depth, -1000.0, 1000.0) || start_depth > end_depth)
            errorInfoDialog("The depth is wrong or the lower bound is greater than the upper bound \n\nTips: -1000 <= depth <= 1000");
        else if (!isFitValue(start_magnitude, 0.0, 10.0) || !isFitValue(end_magnitude, 0.0, 10.0) || start_magnitude > end_magnitude)
            errorInfoDialog("The intensity is wrong or the lower bound is greater than the upper bound \n\nTips: 0 <= intensity <= 10");
        else flag = false;

        return flag;
    }

    /**
     * The event of opening file on menu item
     */
    public void onMenuItemOpenFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please select file source");
        List<String> tempFileType = new LinkedList<>();
        tempFileType.add("*.db");
        tempFileType.add("*.sqlite");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"), new FileChooser.ExtensionFilter("database", tempFileType));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            if (file.getName().contains(".csv"))
                radioButton_fromCSV.setSelected(true);
            if (file.getName().contains(".db") || file.getName().contains(".sqlite"))
                radioButton_fromDB.setSelected(true);
            textField_browse.setText(file.getAbsolutePath());
        }
    }

    /**
     * The event that selectiong data from website on menu item
     */
    public void onMenuItemDataFromWebsite() {
        radioButton_fromWeb.setSelected(true);
    }

    /**
     * The event of exiting on menu item
     */
    public void onMenuItemExit() {
        System.exit(0);
    }
    /**
     * The event of the query button
     */
    public void onButtonQuery() {
        getData();
        if (!isErrorCondition()) {
            if (isChangedFile) {
                if (radioButton_fromCSV.isSelected()) {
                    if (textField_browse.getText().contains(".csv")) {
                        readDataFromCsv(textField_browse.getText());
                        readDataFromDatabase("earthquakes-2.db", "quakes");
                    }else {
                        errorInfoDialog("Please select the csv file");
                    }

                } else if (radioButton_fromDB.isSelected()) {
                    if ((textField_browse.getText().contains(".db")) || (textField_browse.getText().contains(".sqlite")))
                        readDataFromDatabase(textField_browse.getText(), "quakes");
                    else
                        errorInfoDialog("Please select the database file");
                } else if (radioButton_fromWeb.isSelected()) {
                    scrapingDataFromWebsite(2);
                    readDataFromDatabase("earthquakes-2.db", "quakes");
                }
                isChangedFile = false;
            }
            showTable();
            showMap();
            showBarChart();
        }
    }

    /**
     * To clear the events of buttons
     */
    public void onButtonClear() {
        data.clear();
        tableView_table.setItems(data);
        datePicker_start.getEditor().clear();
        datePicker_end.getEditor().clear();
        textField_latitude_start.clear();
        textField_latitude_end.clear();
        textField_longitude_start.clear();
        textField_longitude_end.clear();
        textField_depth_start.clear();
        textField_depth_end.clear();
        textField_magnitude_start.clear();
        textField_magnitude_end.clear();
        ImageView mapView = imageView_map;
        pane_image.getChildren().clear();
        pane_image.getChildren().add(mapView);
        label_state.setText("");
        barChart_bc.getData().clear();

    }

    /**
     * To browse the events of buttons
     */
    public void onButtonBrowse() {
        if (radioButton_fromCSV.isSelected()) {
            filePostFix = new LinkedList<>();
            fileType = "CSV";
            filePostFix.add("*.csv");
        } else if (radioButton_fromDB.isSelected()) {
            filePostFix = new LinkedList<>();
            fileType = "Database";
            filePostFix.add("*.db");
            filePostFix.add("*.sqlite");
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please the file source");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(fileType, filePostFix));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            textField_browse.setText(file.getAbsolutePath());
        }
    }

    /**
     * To initialize the GUI
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        radioButtonConfig();
        comboBoxConfig();
        for (int i = 0; i < 5; i++) {
            numOfMagnitude.put(i, new LinkedList<>());
        }

        tableColumn_ID.setCellValueFactory(cellDate -> cellDate.getValue().idProperty());
        tableColumn_magnitude.setCellValueFactory(cellDate -> cellDate.getValue().magnitudeProperty());
        tableColumn_Date.setCellValueFactory(cellDate -> cellDate.getValue().UTC_dateProperty());
        tableColumn_latitude.setCellValueFactory(cellDate -> cellDate.getValue().latitudeProperty());
        tableColumn_longitude.setCellValueFactory(cellDate -> cellDate.getValue().longitudeProperty());
        tableColumn_depth.setCellValueFactory(cellDate -> cellDate.getValue().depthProperty());
        tableColumn_region.setCellValueFactory(cellDate -> cellDate.getValue().regionProperty());
        tableColumn_area_id.setCellValueFactory(cellDate -> cellDate.getValue().area_idProperty());
    }
}
