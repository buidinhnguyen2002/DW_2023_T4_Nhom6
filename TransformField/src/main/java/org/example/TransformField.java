package org.example;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransformField {

    private ConfigReader configReader;
    static String urlControl;
    String userControl;
    String passControl;
    String urlStaging;
    String userStaging;
    String passStaging;
    String moduleLoad;
    String moduleTransform;
    String moduleProcess;
    String moduleSuccess;
    String filePathLogs;
    Connect controllerConnection;
    String moduleFile;

    public TransformField(ConfigReader configReader){
        this.configReader = configReader;
        loadConfig();
    }

    // 2. Load module config
    public void loadConfig(){
        urlControl = configReader.getProperty(ConfigReader.ConfigurationProperty.STAGING_CONTROL_URL.getPropertyName());
        userControl = configReader.getProperty(ConfigReader.ConfigurationProperty.STAGING_CONTROL_USERNAME.getPropertyName());
        passControl = configReader.getProperty(ConfigReader.ConfigurationProperty.STAGING_CONTROL_PASSWORD.getPropertyName());

        urlStaging = configReader.getProperty(ConfigReader.ConfigurationProperty.STAGING_CONTROL_URL.getPropertyName());
        userStaging = configReader.getProperty(ConfigReader.ConfigurationProperty.STAGING_USERNAME.getPropertyName());
        passStaging = configReader.getProperty(ConfigReader.ConfigurationProperty.STAGING_PASSWORD.getPropertyName());

        moduleLoad = configReader.getProperty(ConfigReader.ConfigurationProperty.MODULE_LOAD_STAGING.getPropertyName());
        moduleTransform = configReader.getProperty(ConfigReader.ConfigurationProperty.MODULE_TRANSFORM.getPropertyName());
        moduleProcess = configReader.getProperty(ConfigReader.ConfigurationProperty.MODULE_PROCESS.getPropertyName());
        moduleSuccess = configReader.getProperty(ConfigReader.ConfigurationProperty.MODULE_SUCCESS.getPropertyName());
        moduleFile = configReader.getProperty(ConfigReader.ConfigurationProperty.MODULE_FILE_LOGS_ERROR.getPropertyName());
    }

    public boolean checkPreviousProgress(){
        boolean result;
        // 3. Connect to database control
        controllerConnection = new Connect(urlControl, userControl, passControl);
        Connection connectControl = controllerConnection.getConnection();

        // 4.Checking connection to database control
        if (connectControl == null){

            // 4.1.Insert new record failed into file log
            controllerConnection.writeLogToFile(moduleFile, "fail", "connect control failed");
            return false; // kết thúc
        }

        // 4.2. Select * from control.logs where event = "Load data to Staging" and create_at = CURRENT_DATE() and status = "successful"
        String query = "SELECT * FROM control.logs WHERE event = '" + moduleLoad + "' AND create_at = CURRENT_DATE() AND status = '" + moduleSuccess + "'";
        try{
            Statement stmt = connectControl.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            int column = metaData.getColumnCount();

            // 5. Check query results
            if (rs.next()){
                result = true;
                String test = "";
                for(int i = 1; i <= column; i++){
                    String columnName = metaData.getColumnName(i);
                    String data = rs.getString(columnName);
                    test += columnName + " " + data + "\t";
                }
            } else{
                result = false;
            }
        } catch(SQLException e){
            throw new RuntimeException();
        }
        return result;
    }

    public void execute(){
        // result = false -> kết thúc
        if (!checkPreviousProgress()) {
            return;
        }

        // result = true
        // 6. Insert into control.logs(event, status) values ('Transform field', 'in process')
        insertLogProcess();

        // 7. Connect database staging
        Connect connectStaging = new Connect(urlStaging, userStaging, passStaging);

        try {
            Connection connectionStaging = connectStaging.getConnection();

            // 8. Checking connection to database staging
            // fail
            if (connectionStaging == null) {

                // 8.1. Insert control.logs(event, status, note) values ('Transform field', 'fail', 'connect to staging failed')
                writeLog();

                // kết thúc
                return;
            }

            // 9. Truncate table News and Category
            truncateTableNews();
            truncateTableCategory();
            System.out.println("Tables 'News' and 'Category' truncated successfully.");

            // 10. Get rows form StagingNews table
            ResultSet stagingNewsResultSet = getStagingNewsRows(connectionStaging);

            while (stagingNewsResultSet.next()) {
                String createAt = stagingNewsResultSet.getString("create_at");

                // 11. Check if create_at format is dd-MM-yyyy
                // false
                if (!isValidDateFormat(createAt, "dd-MM-yyyy")) {
                    // 11.1. Insert new record failed into file log
                    controllerConnection.writeLogToFile(moduleFile, "fail", "Date format error");
                    return; // kết thúc
                }
            }

            // true
            // 11.2. Insert into News and Category tables
            insertIntoNewsAndCategory(stagingNewsResultSet, connectionStaging);

            // 12. Insert into control.logs(event, status, create_at) values ('Transform', 'successful', current_date())
            insertLogSuccess();

            // 13. Close all connect
            connectionStaging.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertLogProcess(){
        Connection connection = controllerConnection.getConnection();
        String query = "INSERT INTO control.logs(event, status) VALUES (?, ?)";
        try{
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setString(1, moduleTransform);
            pre.setString(2, moduleProcess);
            pre.executeUpdate();
            pre.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertLogSuccess(){
        Connection connection = controllerConnection.getConnection();
        String query = "INSERT INTO control.logs(event, status, create_at) VALUES (?, ?, current_date())";
        try{
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setString(1, moduleTransform);
            pre.setString(2, moduleSuccess);
            pre.executeUpdate();
            pre.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeLog(){
        Connection connection = controllerConnection.getConnection();
        String query = "INSERT control.logs(event, status, note) VALUES (?, ?, ?)";
        try{
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setString(1, moduleTransform);
            pre.setString(2, "fail");
            pre.setString(3, "connect to staging failed");
            pre.executeUpdate();
            pre.close();
        } catch(SQLException e){
            throw new RuntimeException();
        }
    }

    public void truncateTableNews(){
        String query = "TRUNCATE TABLE `staging`.`News`";
        try{
            Connection connection = controllerConnection.getConnection();
            PreparedStatement pre = connection.prepareStatement(query);
            pre.executeUpdate();
            pre.close();
        } catch(SQLException e){
            throw new RuntimeException();
        }
    }

    public void truncateTableCategory() {
        String tableName = "category";
        String query = "TRUNCATE TABLE `staging`.`" + tableName + "`";
        try {
            Connection connection = controllerConnection.getConnection();
            PreparedStatement pre = connection.prepareStatement(query);
            pre.executeUpdate();
            pre.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error truncating Category table: " + e.getMessage(), e);
        }
    }

    private static ResultSet getStagingNewsRows(Connection connection) throws SQLException {
        // Get rows from StagingNews table
        Statement statement = connection.createStatement();
        return statement.executeQuery("SELECT * FROM `staging`.`StagingNews`");
    }

    private static void insertIntoNewsAndCategory(ResultSet resultSet, Connection connection) throws SQLException {
        // Insert into News and Category tables
        PreparedStatement insertCategoryStatement = connection.prepareStatement(
                "INSERT INTO `staging`.`category` (title, create_at, update_at) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

        PreparedStatement insertNewsStatement = connection.prepareStatement(
                "INSERT INTO `staging`.`News` (title, image, categoryId, desciption, content, author, tags, create_at, update_at, create_by, update_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        try {
            while (resultSet.next()) {
                String categoryTitle = resultSet.getString("category");
                String createAt = resultSet.getString("create_at");
                String updateAt = resultSet.getString("update_at");

                int categoryId = getCategoryId(connection, categoryTitle, createAt, updateAt);

                if (categoryId == -1) {

                    // Insert into Category
                    insertCategoryStatement.setString(1, resultSet.getString("category"));
                    insertCategoryStatement.setString(2, resultSet.getString("create_at"));
                    insertCategoryStatement.setString(3, resultSet.getString("update_at"));

                    int affectedRowsCategory = insertCategoryStatement.executeUpdate();
                    if (affectedRowsCategory > 0) {
                        ResultSet generatedKeysCategory = insertCategoryStatement.getGeneratedKeys();
                        if (generatedKeysCategory.next()) {
                            categoryId = generatedKeysCategory.getInt(1);
                        }
                    } else{
                        categoryId = getCategoryId(connection, resultSet.getString("category"), resultSet.getString("create_at"), resultSet.getString("update_at"));
                    }
                }
                // Insert into News
                insertNewsStatement.setString(1, resultSet.getString("title"));
                insertNewsStatement.setString(2, resultSet.getString("image"));
                insertNewsStatement.setInt(3, categoryId);
                insertNewsStatement.setString(4, resultSet.getString("desciption"));
                insertNewsStatement.setString(5, resultSet.getString("content"));
                insertNewsStatement.setString(6, resultSet.getString("author"));
                insertNewsStatement.setString(7, resultSet.getString("tags"));
                insertNewsStatement.setString(8, resultSet.getString("create_at"));
                insertNewsStatement.setString(9, resultSet.getString("update_at"));
                insertNewsStatement.setString(10, resultSet.getString("create_by"));
                insertNewsStatement.setString(11, resultSet.getString("update_by"));

                insertNewsStatement.executeUpdate();
            }
        } finally {
            insertNewsStatement.close();
            insertCategoryStatement.close();
        }
    }

    private static int getCategoryId(Connection connection, String categoryTitle, String createAt, String updateAt) throws SQLException {
        // Check if category already exists
        PreparedStatement selectCategoryStatement = connection.prepareStatement(
                "SELECT id FROM staging.category WHERE title = ? AND create_at = ? AND update_at = ?");
        selectCategoryStatement.setString(1, categoryTitle);
        selectCategoryStatement.setString(2, createAt);
        selectCategoryStatement.setString(3, updateAt);

        ResultSet categoryResultSet = selectCategoryStatement.executeQuery();

        if (categoryResultSet.next()) {
            // Category already exists, return its ID
            return categoryResultSet.getInt("id");
        } else {
            // Category doesn't exist
            return -1;
        }
    }

    public boolean isValidDateFormat(String dateStr, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);

        try {
            // Parsing ngày theo định dạng
            Date date = sdf.parse(dateStr);
            System.out.println("Date: " + sdf.format(date)); // Kiểm tra in ngày đã parse (optional)
        } catch (ParseException e) {
            // Nếu có lỗi ParseException, tức là định dạng không hợp lệ
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        ConfigReader conf = new ConfigReader();
        TransformField transform = new TransformField(conf);
        transform.execute();
    }
}
