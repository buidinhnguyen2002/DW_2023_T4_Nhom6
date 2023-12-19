package org.example;

import java.sql.*;

public class Main {
    private ConfigReader configReader;
    private ConnectDB connectDBControl;
    String urlControl;
    String userControl;
    String passControl;

    String urlDW;
    String userDW;
    String passDW;

    String urlStaging;
    String userStaging;
    String passStaging;
    String moduleName;
    String columns;
    String columnDimTime;
    String columnDimArticle;
    String columnDimAuthor;
    String columnDimNewsCategory;
    String columnsFactNewsArticle;
    String filePathLogs;
    String previousModule;
    int idLog;
    public Main(ConfigReader configReader) {
        this.configReader = configReader;
        // 2. Load config module
        loadConfig();
    }
    // 2. load config module
    public void loadConfig() {
        // load config module
        moduleName = configReader.getProperty(ConfigReader.ConfigurationProperty.MODULE_LOAD_TO_DATA_WAREHOUSE.getPropertyName());
        columns = configReader.getProperty(ConfigReader.ConfigurationProperty.MODULE_COLUMNS_NEW_ARTICLES.getPropertyName());
        columnDimTime = configReader.getProperty(ConfigReader.ConfigurationProperty.DATA_WAREHOUSE_INSERT_DIM_TIME.getPropertyName());
        columnDimArticle = configReader.getProperty(ConfigReader.ConfigurationProperty.DATA_WAREHOUSE_INSERT_DIM_ARTICLE.getPropertyName());
        columnDimAuthor = configReader.getProperty(ConfigReader.ConfigurationProperty.DATA_WAREHOUSE_INSERT_DIM_AUTHOR.getPropertyName());
        columnDimNewsCategory = configReader.getProperty(ConfigReader.ConfigurationProperty.DATA_WAREHOUSE_INSERT_DIM_NEWS_CATEGORY.getPropertyName());
        columnsFactNewsArticle = configReader.getProperty(ConfigReader.ConfigurationProperty.DATA_WAREHOUSE_INSERT_FACT_NEWS_ARTICLE.getPropertyName());
        filePathLogs = configReader.getProperty(ConfigReader.ConfigurationProperty.MODULE_FILE_LOGS_ERROR.getPropertyName());
        previousModule = configReader.getProperty(ConfigReader.ConfigurationProperty.MODULE_PREVIOUS_MODULE.getPropertyName());

        // load config dbControl
        urlControl = configReader.getProperty(ConfigReader.ConfigurationProperty.STAGING_CONTROL_URL.getPropertyName());
        userControl = configReader.getProperty(ConfigReader.ConfigurationProperty.STAGING_CONTROL_USERNAME.getPropertyName());
        passControl = configReader.getProperty(ConfigReader.ConfigurationProperty.STAGING_CONTROL_PASSWORD.getPropertyName());

        // load config db DW
        urlDW = configReader.getProperty(ConfigReader.ConfigurationProperty.DATA_WAREHOUSE_URL.getPropertyName());
        userDW = configReader.getProperty(ConfigReader.ConfigurationProperty.DATA_WAREHOUSE_USERNAME.getPropertyName());
        passDW= configReader.getProperty(ConfigReader.ConfigurationProperty.DATA_WAREHOUSE_PASSWORD.getPropertyName());

        // load config db Staging
        urlStaging = configReader.getProperty(ConfigReader.ConfigurationProperty.STAGING_URL.getPropertyName());
        userStaging = configReader.getProperty(ConfigReader.ConfigurationProperty.STAGING_USERNAME.getPropertyName());
        passStaging = configReader.getProperty(ConfigReader.ConfigurationProperty.STAGING_PASSWORD.getPropertyName());
    }
    public boolean checkPreviousProgress(){
        boolean result = false;
        // 3. connect database control
        connectDBControl = new ConnectDB(urlControl,userControl, passControl, filePathLogs);
        Connection connectionControl = connectDBControl.getConnection();
        // 4. Checking connection to database control
        if(connectionControl == null){
            //4.1 Insert new record failed into file log
            // ghi log vào file nếu kết nối thất bại
            connectDBControl.writeLogToFile(filePathLogs, "fail", "connect control failed");
            return false; //  kết thúc chương trình
        }
        // 4.2 Select  * from logs where event = "Transform field" and DATE(create_at) = CURDATE() and status="successful"
        String queryPreviousProcess = "SELECT * FROM logs where event='" + previousModule + "' AND DATE(create_at) = CURDATE() AND status='successful'";
                try {
            Statement stmtControl = connectionControl.createStatement();
            ResultSet rs = stmtControl.executeQuery(queryPreviousProcess);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            // 5. Check query results
            if(rs.next()){
                result = true;
                String test = "";
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String data = rs.getString(columnName);
                    test += columnName + " " + data + "\t";
                }
                System.out.println(test);
            }else{
                result = false;
            }
            //
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    private boolean checkProcessEverRun() {
        boolean result = false;
        Connection connectionControl = connectDBControl.getConnection();
        // 5.1 Select  * from logs where event = "load to data warehouse" and DATE(create_at) = CURDATE() and status="successful"
        String queryProcess = "SELECT * FROM logs where event='" + moduleName + "' AND DATE(create_at) = CURDATE() AND status='successful'";
        try {
            Statement stmtControl = connectionControl.createStatement();
            ResultSet rs = stmtControl.executeQuery(queryProcess);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            // 6. Check query results
            if(rs.next()){
                result = true;
            }else{
                result = false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public void executeApp() {
        // 5. Check query results
        if (!checkPreviousProgress()) {
            return;
        }
        // 6. Check query results
        if (checkProcessEverRun()) {
            return;
        }
        // insert logs
        // 6.1 Insert new record into table control.log with event="load to data warehouse",status="in process" (INSERT INTO logs(event, status) VALUES ('Load to data warehouse','in process'))
        insertLogsProcess("in process", "");

        try {
            // 7. Connect database staging
            ConnectDB connectStaging = new ConnectDB(urlStaging, userStaging, passStaging, filePathLogs, idLog, connectDBControl.getConnection());
            Connection connectionDM = connectStaging.getConnection();
            // 8. Checking connection to staging
            if(connectionDM == null) {
                // 8. 1 Insert new record into table control.log with event="load to data warehouse",status="fail", note="content error"
                //(INSERT INTO logs(event, status,note) VALUES ('load to data warehouse','fail', 'connect database staging failed'))
                insertLogsProcess("fail", "connect staging failed");
                return;
            }
            // Truy vấn SQL để lấy dữ liệu từ bảng trong Staging
            // 8.2 Get data in  staging.news, staging.category
            // (SELECT n.title, n.image, c.title as category, n.description, n.content, n.author, n.tags, n.create_at as date FROM news n inner join category c on n.categoryId = c.id)
            String sqlSelect = configReader.getProperty(ConfigReader.ConfigurationProperty.STAGING_QUERY_DATA.getPropertyName());
            Statement stmtDW = connectionDM.createStatement();
            ResultSet rs = stmtDW.executeQuery(sqlSelect);
            // 9. Connect database data_warehouse
            ConnectDB connectDW = new ConnectDB(urlDW, userDW, passDW, filePathLogs, idLog, connectDBControl.getConnection());
            Connection connectionDW = connectDW.getConnection();
            // 10. Checking connection to data_warehouse
            if(connectionDW == null) {
                //10.1 Insert new record into table control.log with event="load to data warehouse",status="fail", note="content error"
                //(INSERT INTO logs(event, status,note) VALUES ('load to data warehouse','fail', 'connect data_warehouse failed'))
                insertLogsProcess("fail", "connect data_warehouse failed");
                return;
            }
            // PreparedStatement để chèn dữ liệu vào data_warehouse
            String sqlInsertDimTime = createQueryInsertDimTime("dim_time", columnDimTime);
            String sqlInsertDimArticle = createQueryInsertDim("dim_article", columnDimArticle);
            String sqlInsertDimAuthor = createQueryInsertDim("dim_author", columnDimAuthor);
            String sqlInsertDimNewsCategory = createQueryInsertDim("dim_news_category", columnDimNewsCategory);
            String sqlInsertFactNewsArticles = createQueryInsertDim("fact_news_articles", columnsFactNewsArticle);
            PreparedStatement pstmtDimTime = connectionDW.prepareStatement(sqlInsertDimTime, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement pstmtDimArticle = connectionDW.prepareStatement(sqlInsertDimArticle, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement pstmtDimAuthor = connectionDW.prepareStatement(sqlInsertDimAuthor, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement pstmtDimNewsCategory = connectionDW.prepareStatement(sqlInsertDimNewsCategory, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement pstmtFactNewsArticles = connectionDW.prepareStatement(sqlInsertFactNewsArticles, Statement.RETURN_GENERATED_KEYS);
            String[] columnsArr = columns.split(",");
            String[] columnsArrDimTime = columnDimTime.split(",");
            String[] columnsArrDimArticle = columnDimArticle.split(",");
            String[] columnsArrDimNewsCategory = columnDimNewsCategory.split(",");
            String[] columnsArrDimAuthor = columnDimAuthor.split(",");
            String[] columnFactNewsArticle = columnsFactNewsArticle.split(",");
            ResultSet generatedKeys;
            // Duyệt qua kết quả từ staging và chèn vào data_warehouse
            // // 10.2 Insert rows into table dim_time, dim_article, dim_news_category, dim_author, fact_news_articles
            while (rs.next()) {
                int dimTimeId =0, dimAuthorId = 0, dimArticleId = 0, dimNewCategoryId = 0;
                // Lấy dữ liệu từ kết quả truy vấn DW và chèn vào DM
                for(int i=0; i< columnsArrDimNewsCategory.length; i++){
                    String column = "category";
                    pstmtDimNewsCategory.setString(i+1, rs.getString(column));
                }
                pstmtDimNewsCategory.executeUpdate();
                generatedKeys = pstmtDimNewsCategory.getGeneratedKeys();
                if (generatedKeys.next()) {
                    dimNewCategoryId = generatedKeys.getInt(1);
                }
                pstmtFactNewsArticles.setInt(1, dimNewCategoryId);

                for(int i=0; i< columnsArrDimAuthor.length; i++){
                    String column = "author";
                    pstmtDimAuthor.setString(i+1, rs.getString(column));
                }
                pstmtDimAuthor.executeUpdate();
                generatedKeys = pstmtDimAuthor.getGeneratedKeys();
                if (generatedKeys.next()) {
                    dimAuthorId = generatedKeys.getInt(1);
                }
                pstmtFactNewsArticles.setInt(2, dimAuthorId);

                String columnDate = columnsArrDimTime[0];
                String date = rs.getString(columnDate);
                pstmtDimTime.setString(1, date);
                pstmtDimTime.setString(2, date);
                pstmtDimTime.setString(3, date);
                pstmtDimTime.setString(4, date);
                pstmtDimTime.executeUpdate();
                generatedKeys = pstmtDimTime.getGeneratedKeys();
                if (generatedKeys.next()) {
                    dimTimeId = generatedKeys.getInt(1);
                }
                pstmtFactNewsArticles.setInt(3, dimTimeId);
                for(int i=0; i< columnsArrDimArticle.length; i++){
                    String column = columnsArrDimArticle[i];
                    pstmtDimArticle.setString(i+1, rs.getString(column));
                }
                pstmtDimArticle.executeUpdate();
                generatedKeys = pstmtDimArticle.getGeneratedKeys();
                if (generatedKeys.next()) {
                    dimArticleId = generatedKeys.getInt(1);
                }
                pstmtFactNewsArticles.setInt(4, dimArticleId);
                pstmtFactNewsArticles.executeUpdate();
                // Thực hiện chèn dữ liệu vào Data Mart
            }
            // 11. Insert new record into table control.log with event="load to data warehouse",status="successful"
            //(INSERT INTO logs(event, status) VALUES ('load to data warehouse','successful'))
            insertLogsProcess("successful", "");
            // Đóng các kết nối
            // 12. Close all connect database
            rs.close();
            stmtDW.close();
            pstmtDimTime.close();
            connectionDW.close();
            connectionDM.close();
//            connectDBControl.getConnection().close();
            System.out.println("Data transfer from Staging to DW completed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String createQueryInsertDim(String tableName, String columns) {
        String valuesField = "";
        String values = "";
//        String[] columnsArr = columnDimTime.split(",");
        String[] columnsArr = columns.split(",");
        for(int i=0; i< columnsArr.length - 1; i++){
            valuesField += "`"+columnsArr[i]+"`,";
            values += "?,";
        }
        valuesField += "`"+columnsArr[columnsArr.length-1]+"`";
        values += "?";
        String result = "INSERT INTO "+ tableName +"("+valuesField+") VALUES ("+values+")" ;
        return result;
    }

    private String createQueryInsertDimTime(String tableName, String columns) {
        String valuesField = "";
        String values = "";
        String[] columnsArr = columns.split(",");
        for(int i=0; i< columnsArr.length - 1; i++){
            valuesField += "`"+columnsArr[i]+"`,";

        }
        values += "?,DAYOFWEEK(?),MONTH(?), YEAR(?)";
        valuesField += "`"+columnsArr[columnsArr.length-1]+"`";
        String result = "INSERT INTO "+ tableName +"("+valuesField+") VALUES ("+values+")" ;
        return result;
    }

    private void updateStatusProcess(String status) {
        String sqlUpdate = "UPDATE logs SET status=? WHERE id=?";
        Connection connectionControl = connectDBControl.getConnection();
        try {
            PreparedStatement pstmtControl = connectionControl.prepareStatement(sqlUpdate);
            pstmtControl.setString(1, status);
            pstmtControl.setInt(2, idLog);
            pstmtControl.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    private void insertLogsProcess(String status, String note) {
        Connection connection = connectDBControl.getConnection();
        String sqlInsert = "INSERT INTO logs(event, status, note) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, moduleName);
//            preparedStatement.setString(2, "in process");
            preparedStatement.setString(2, status);
            preparedStatement.setString(3, note);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                idLog = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String createQueryInsertToDataMart(String tableName) {
        String valuesField = "";
        String values = "";
        String[] columnsArr = columns.split(",");
        for(int i=0; i< columnsArr.length - 1; i++){
            valuesField += "`"+columnsArr[i]+"`,";
            values += "?,";
        }
        valuesField += "`"+columnsArr[columnsArr.length-1]+"`";
        values += "?";
        String result = "INSERT INTO "+ tableName +"("+valuesField+") VALUES ("+values+")" ;
        return result;
    }

    public static void main(String[] args) {
        ConfigReader configReader = new ConfigReader();
        Main main = new Main(configReader);
        // 1. Run LoadDwTool.jar
        main.executeApp();
    }
}