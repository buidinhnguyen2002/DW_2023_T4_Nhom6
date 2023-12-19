import getConnection.ConfigReader;
import getConnection.GetConnection;
import getConnection.LogError;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoadSourceToFile {
    private ConfigReader configReader;
    GetConnection getConnection;
    static String url;
    String user;
    String password;
    String csv;
    String moduleFile;
    String moduleSuccess;

    public LoadSourceToFile(ConfigReader configReader) {
        this.configReader = new ConfigReader();
        loadConfig();
    }

    public void loadConfig() {
        url = configReader.getProperty("url");
        user = configReader.getProperty("username");
        password = configReader.getProperty("password");
    }

    public void run() {
        ConfigReader configReader = new ConfigReader();
        Connection connection = configReader.getConnection();
        //2.1 check connect to database
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                //2.2 Load config source path
                ResultSet resultSet = statement.executeQuery("SELECT source_path FROM data_file_configs");
                if (resultSet.next()) {
                    System.out.println(resultSet.getString("source_path"));
                }

                // 2.3 add source path to table data_file_configs
                String sourcePath = "https://vnexpress.net/the-thao";
                insertSourcePath(sourcePath);
                // 2.4 add news row to table data_files with id of data_file_configs
                insertDataFiles();
                // 2.5 load data from source to file
                CrawDataFromSource crawDataFromSource = new CrawDataFromSource();
                // 2.5.1 crawl data from source
                String data = crawDataFromSource.crawlData(sourcePath);
                // 2.5.2 save data to file json
                crawDataFromSource.saveDataToFileJson(data, "Load_from_Source_To_File/src/main/java/data/data.json");
                // 2.5.3 parse data to csv file
                crawDataFromSource.parseToCSVFile("Load_from_Source_To_File/src/main/java/data/data.json");
                // 2.6 update status to table data_files
                    udpateStatusDataFiles();

            } catch (SQLException e) {
                LogError.log(e.getMessage());
            }
        } else {
            LogError.log("Failed to make connection to database!");
        }

    }

    // 2.6 update status to table data_files
    private void udpateStatusDataFiles() {
        getConnection = new GetConnection(url, user, password);
        String query = "UPDATE control.data_files SET status = ?, note = ?, updated_at = current_date(), updated_by = ? WHERE id = ?";
        moduleFile = "Load from Source to File";
        moduleSuccess = "Success";
        // 2.6.1 check update successfully status to table data_files
        try {
            PreparedStatement pre = getConnection.getConnection().prepareStatement(query);
            pre.setString(1, moduleSuccess);
            pre.setString(2, "Data import success");
            pre.setString(3, "root");
            pre.setInt(4, 1);
            pre.executeUpdate();
            pre.close();
        } catch (Exception e) {
            LogError.log(e.getMessage());
        }
    }

    // 2.3 add source path to table data_file_configs
    private void insertSourcePath(String sourcePath) {
        String csvFile = "VNExpress" + new SimpleDateFormat("dd_MM_yyyy").format(new Date());
        String location = "D:\\data_warehouse\\DW_2023_T4_Nhom6\\Load_from_Source_To_File\\src\\main\\java\\data\\" + csvFile + ".csv";
        String moduleFile = "Load from Source to File";
        String moduleSuccess = "Success";
        getConnection = new GetConnection(url, user, password);
        String query = "INSERT INTO control.data_file_configs(source_path, location, status, note, created_at, updated_at, created_by, updated_by)" +
                "VALUES (?, ?, ?, ?, current_date(), current_date(), ?, ?)";
        // 2.3.1 check insert successfully source path to table data_file_configs
        try {
            PreparedStatement pre = getConnection.getConnection().prepareStatement(query);
            pre.setString(1, sourcePath);
            pre.setString(2, location);
            pre.setString(3, moduleSuccess);
            pre.setString(4, "Data import success");
            pre.setString(5, "root");
            pre.setString(6, "root");
            pre.executeUpdate();
            pre.close();
        } catch (Exception e) {
            LogError.log(e.getMessage());
        }
    }
   // 2.4 add news row to table data_files with id of data_file_configs
    public void insertDataFiles() {
        getConnection = new GetConnection(url, user, password);
        String query = "INSERT INTO control.data_files(name, row_count, status, note, created_at, updated_at, created_by, updated_by)" +
                "VALUES (?, ?, ?, ?, current_date(), current_date(), ?, ?)";
        moduleFile = "Load from Source to File";
        moduleSuccess = "Success";
        // 2.4.1 check insert successfully source path to table data_file_configs
        try {
            PreparedStatement pre = getConnection.getConnection().prepareStatement(query);
            pre.setString(1, csv + new SimpleDateFormat("dd_MM_yyyy").format(new java.util.Date()));
            pre.setInt(2, countCSVLines(loadFilePath() + csv + new SimpleDateFormat("dd_MM_yyyy").format(new Date()) + ".csv"));
            pre.setString(3, moduleSuccess);
            pre.setString(4, "Data import success");
            pre.setString(5, "root");
            pre.setString(6, "root");
            pre.executeUpdate();
            pre.close();
        } catch (Exception e) {
            LogError.log(e.getMessage());
        }
    }

    public int countCSVLines(String filePath) throws IOException {
        int lineCount = 0;

        // Mở file để đọc
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            br.readLine();
            // Đọc từng dòng
            while ((line = br.readLine()) != null) {
                lineCount++;
            }
        }

        return lineCount;
    }

    public String loadFilePath() {
        getConnection = new GetConnection(url, user, password);
        String filePath = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        if (filePath == null || filePath.isEmpty()) {
            getConnection.writeLogToFile(moduleFile, "error", "Invalid file path");
        }
        try {
            ps = getConnection.getConnection().prepareStatement("SELECT location FROM control.data_file_configs WHERE create_at = \"2023-12-14 08:50:55\" ORDER BY create_at DESC LIMIT 1");
            rs = ps.executeQuery();
            if (rs.next()) {
                // Lấy giá trị từ cột "location"
                filePath = rs.getString("location");
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return filePath;
    }

    // 1. run main
    public static void main(String[] args) {
        ConfigReader configReader = new ConfigReader();
        // 1.1 load config
        LoadSourceToFile loadSourceToFile = new LoadSourceToFile(configReader);
        // 2 run
        loadSourceToFile.run();
    }
}
