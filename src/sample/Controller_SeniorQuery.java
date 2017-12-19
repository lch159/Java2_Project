package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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
    private ObservableList<Earthquake> data = FXCollections.observableArrayList();//用于绑定到tableview的数据
    private static ArrayList<String[]> table;//存储查询到的表格数据


    //判断文本框内是否为空
    private boolean isContentEmpty(TextField textField) {
        return textField.getText().isEmpty();
    }

    private boolean isContentEmpty(DatePicker datePicker) {
        return datePicker.getEditor().getText().isEmpty();
    }

    private String getStart_Date() {
        return isContentEmpty(datePicker_start) ? "2017-01-01" : datePicker_start.getEditor().getText().trim();
    }

    private String getEnd_Date() {

        return isContentEmpty(datePicker_end) ? "2017-12-31" : datePicker_end.getEditor().getText().trim();
    }

    private Double getStart_latitude() {
        return isContentEmpty(textField_latitude_start) ? -90 : Double.parseDouble(textField_latitude_start.getText().trim());
    }

    private Double getEnd_latitude() {

        return isContentEmpty(textField_latitude_end) ? 90 : Double.parseDouble(textField_latitude_end.getText().trim());
    }

    private Double getStart_longitude() {
        return isContentEmpty(textField_longitude_start) ? -180 : Double.parseDouble(textField_longitude_start.getText().trim());
    }

    private Double getEnd_longitude() {
        return isContentEmpty(textField_longitude_end) ? 180 : Double.parseDouble(textField_longitude_end.getText().trim());
    }

    private Double getStart_depth() {
        return isContentEmpty(textField_depth_start) ? -1000 : Double.parseDouble(textField_depth_start.getText().trim());
    }

    private Double getEnd_depth() {
        return isContentEmpty(textField_depth_end) ? 1000 : Double.parseDouble(textField_depth_end.getText().trim());
    }

    private Double getStart_magnitude() {
        return isContentEmpty(textField_magnitude_start) ? 0 : Double.parseDouble(textField_magnitude_start.getText().trim());
    }

    private Double getEnd_magnitude() {
        return isContentEmpty(textField_magnitude_end) ? 10 : Double.parseDouble(textField_magnitude_end.getText().trim());
    }

    private void setStartDate(String date) {
        String[] startDate = date.split("-");
        start_year = Double.parseDouble(startDate[0]);
        start_month = Double.parseDouble(startDate[1]);
        start_day = Double.parseDouble(startDate[2]);
    }

    private void setEndDate(String date) {
        String[] endDate = date.split("-");
        end_year = Double.parseDouble(endDate[0]);
        end_month = Double.parseDouble(endDate[1]);
        end_day = Double.parseDouble(endDate[2]);
    }


    //将查询到的数据存入已经绑定的data中
    private void showList() {
        if (!data.isEmpty())
            data.clear();

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
                if (elem.length > 7)
                    stringEarthquake.setArea_id(elem[7]);
                Earthquake earthquake = new Earthquake(stringEarthquake);
                data.add(earthquake);

            }
        }
        tableView_table.setItems(data);
    }


    //判断该次地震的一个数据是否在范围内
    private boolean isFitValue(Double value, Double start, Double end) {

        return value >= start && value <= end;
    }

    //判断地震所有数据是否符合要求
    private boolean isFitValues(String[] elem) {
        String[] tempDate = elem[1].split("[ -]");
        return isFitValue(Double.parseDouble(tempDate[0]), start_year, end_year) && isFitValue(Double.parseDouble(tempDate[1]), start_month, end_month) && isFitValue(Double.parseDouble(tempDate[2]), start_day, end_day) && isFitValue(Double.parseDouble(elem[2]), start_latitude, end_latitude)
                && isFitValue(Double.parseDouble(elem[3]), start_longitude, end_longitude) && isFitValue(Double.parseDouble(elem[4]), start_depth, end_depth) && isFitValue(Double.parseDouble(elem[5]), start_magnitude, end_magnitude);

    }


    //获取文本框内填入的内容
    private void iniData() {
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


    //从csv文件中获取原始数据
    private void readDataFromCsv(String path) {
        try {
            table = new ArrayList<>();
            CsvReader earthquakeFile = new CsvReader(path);
            earthquakeFile.readHeaders();
            while (earthquakeFile.readRecord()) {
                table.add(earthquakeFile.getValues());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //从数据库中获取原始数据
    private void readDataFromDatabase(String databaseName) {
        try {
            table = new ArrayList<>();
            String drivde = "org.sqlite.JDBC";
            Class.forName(drivde);// 加载驱动,连接sqlite的jdbc
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName);


            Statement statement = connection.createStatement();
            ResultSet rSet = statement.executeQuery("select * from quakes");//搜索数据库，将搜索的放入数据集ResultSet中
            while (rSet.next()) {            //遍历这个数据集
                String[] rowData = new String[8];
                for (int i = 1; i <= 8; i++) {
                    rowData[i - 1] = rSet.getString(i);
                }

                table.add(rowData);

            }
            rSet.close();//关闭数据集
            connection.close();//关闭数据库连接
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    //从www.emsc-csem.org/Earthquake/网页抓取数据
    private void readDataFromWebsite(int pages) {


        table = new ArrayList<>();
        Document document = null;
        for (int i = 1; i < pages; i++) {
            try {
                String url = String.valueOf(new StringBuilder("https://www.emsc-csem.org/Earthquake/?view=").append(i));
                document = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert document != null;
            Elements element1 = document.getElementById("tbody").getElementsByTag("tr");
            Pattern pattern = Pattern.compile("[0-9]*");
            for (Element elem : element1) {
                if (pattern.matcher(elem.id()).matches()) {
                    String[] rowData = new String[7];
                    rowData[0] = elem.id();
                    rowData[1] = elem.getElementsByClass("tabev6").get(0).getElementsByTag("a").get(0).text();
                    if (elem.getElementsByClass("tabev2").get(0).text().equals("N"))
                        rowData[2] = elem.getElementsByClass("tabev1").get(0).text();
                    else {
                        rowData[2] = String.valueOf(new StringBuilder("-").append(elem.getElementsByClass("tabev1").get(0).text()));
                    }
                    if (elem.getElementsByClass("tabev2").get(1).text().equals("E"))
                        rowData[3] = elem.getElementsByClass("tabev1").get(1).text();
                    else {
                        rowData[3] = String.valueOf(new StringBuilder("-").append(elem.getElementsByClass("tabev1").get(1).text()));
                    }
                    rowData[4] = elem.getElementsByClass("tabev3").get(0).text();
                    rowData[5] = elem.getElementsByClass("tabev2").get(2).text();
                    rowData[6] = elem.getElementsByClass("tb_region").get(0).text();

                    table.add(rowData);
                }
            }
        }




    }

    //初始化
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        readDataFromCsv("earthquakes.csv");
//        readDataFromDatabase("earthquakes-1.sqlite");
        readDataFromWebsite(3);

        tableColumn_ID.setCellValueFactory(cellDate -> cellDate.getValue().idProperty());
        tableColumn_magnitude.setCellValueFactory(cellDate -> cellDate.getValue().magnitudeProperty());
        tableColumn_Date.setCellValueFactory(cellDate -> cellDate.getValue().UTC_dateProperty());
        tableColumn_latitude.setCellValueFactory(cellDate -> cellDate.getValue().latitudeProperty());
        tableColumn_longitude.setCellValueFactory(cellDate -> cellDate.getValue().longitudeProperty());
        tableColumn_depth.setCellValueFactory(cellDate -> cellDate.getValue().depthProperty());
        tableColumn_region.setCellValueFactory(cellDate -> cellDate.getValue().regionProperty());
        tableColumn_area_id.setCellValueFactory(cellDate -> cellDate.getValue().area_idProperty());
    }


    //错误信息框弹出配置
    private void errorInfoDialog(String info) {
        Alert warning = new Alert(Alert.AlertType.WARNING, info);
        Button warn = new Button();
        warn.setOnAction(e -> warning.showAndWait());
        warning.setTitle("信息有误");
        warning.show();
    }


    //文本框填入内容错误情况
    private boolean isErrorCondition() {

        boolean flag = true;
        if (start_year > end_year || start_month > end_month || start_day > end_day)
            errorInfoDialog("日期填写错误或起始值大于终止值");
        else if (!isFitValue(start_latitude, -90.0, 90.0) || !isFitValue(end_latitude, -90.0, 90.0) || start_latitude > end_latitude)
            errorInfoDialog("填写的纬度不在正确范围内或起始值大于终止值 \n\nTips: -90 <= 纬度 <= 90");
        else if (!isFitValue(start_longitude, -180.0, 180.0) || !isFitValue(end_longitude, -180.0, 180.0) || start_longitude > end_longitude)
            errorInfoDialog("填写的经度不在正确范围内或起始值大于终止值 \n\nTips: -180 <= 经度 <= 180");
        else if (!isFitValue(start_depth, -1000.0, 1000.0) || !isFitValue(start_depth, -1000.0, 1000.0) || start_depth > end_depth)
            errorInfoDialog("填写的深度不在正确范围内或起始值大于终止值 \n\nTips: -1000 <= 纬度 <= 1000");
        else if (!isFitValue(start_magnitude, 0.0, 10.0) || !isFitValue(end_magnitude, 0.0, 10.0) || start_magnitude > end_magnitude)
            errorInfoDialog("填写的强度不在正确范围内或起始值大于终止值 \n\nTips: 0 <= 强度 <= 10");
        else flag = false;

        return flag;
    }

    //查询按钮响应事件
    public void onButtonQuery() {
        iniData();
        if (!isErrorCondition())
            showList();
    }


    //清空按钮响应事件
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
    }

}
