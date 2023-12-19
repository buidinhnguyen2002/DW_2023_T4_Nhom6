import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.CSVWriter;
import getConnection.ConfigReader;
import getConnection.GetConnection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoadSourceToFile {
    private ConfigReader configReader;
    GetConnection connection;
    static String url;
    String user;
    String password;
    String csv;
    String moduleFile;
    String moduleSuccess;
    String moduleLoad;
    String moduleProcess;
    String csvFilePath;


    public LoadSourceToFile(ConfigReader configReader) {
        this.configReader = new ConfigReader();
        loadConfig();
    }

    public void loadConfig() {
        url = configReader.getProperty("url");
        user = configReader.getProperty("username");
        password = configReader.getProperty("password");
        moduleLoad = configReader.getProperty("Module.LoadToFile");
        moduleSuccess = configReader.getProperty("Module.Success");
        moduleFile = configReader.getProperty("Module.FileLogsError");
        moduleProcess = configReader.getProperty("Module.Process");
        csv = "VNEXPRESS_";
        csvFilePath = "D:\\data_warehouse\\DW_2023_T4_Nhom6\\Load_from_Source_To_File\\src\\main\\java\\data\\";
    }

    // 2.5.1 crawl data from source
    public String crawlData(String url) {
        //3.5.1 check connection
        if (url == null || url.isEmpty()) {
            //3.5.2 insert control.logs(event, status, note) values ("Load from Source to File", "fail", "connect to source failed");
            writeLog("connect to source failed");
            // kết thúc chương trình
              return null;
        }
        Document doc = null;
        JsonArray jsonArray = new JsonArray();
        // 2.5.1.2 check successfull crawl data
        try {
            doc = Jsoup.connect(url).get();
            String category = doc.select("div.title-folder > a").text();
            Elements tags = doc.select("ul.ul-nav-folder > li");
            Elements elements = doc.select("article.item-news.item-news-common.thumb-left");
            for (Element element : elements) {
                String title = element.select("h3.title-news > a").attr("title");
                System.out.println(title);
                String image = element.select("picture > img").attr("src");
                String realImg = "";
                if (image.contains("base64")) {
                    realImg = "http://graph.vnecdn.net/" + image.split(",")[1];
                } else {
                    realImg = image;
                }
                String link = element.select("div.thumb-art > a").attr("href");
                if (link == null || link.isEmpty()) {
                    continue;  // Skip this iteration if the link is empty
                }
                String description = element.select("p.description > a").text();
                Document doc2 = Jsoup.connect(link).get();
                String content = doc2.select("article.fck_detail >p.normal").text();
                // write content with several paragraphs
                Elements paragraphs = doc2.select("article.fck_detail >p.normal");
                for (Element paragraph : paragraphs) {
                    content += paragraph.text();
                }
                String author = doc2.select("p > strong").text();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("title", title);
                jsonObject.addProperty("image", realImg);
                jsonObject.addProperty("category", category);
                jsonObject.addProperty("description", description);
                jsonObject.addProperty("content", content);
                jsonObject.addProperty("author", author);
                JsonArray jsonTags = new JsonArray();
                for (Element tag : tags) {
                    String tagName = tag.select("a").text();
                    jsonTags.add(tagName);
                }
                jsonObject.add("tags", jsonTags);
                jsonArray.add(jsonObject);
            }
            return jsonArray.toString();
        } catch (IOException e) {
           writeLog(e.getMessage());
        }
        Gson gson = new Gson();
        return gson.toJson(jsonArray);
    }

    // 2.5.2 save data to file json
    public void saveDataToFileJson(String data, String path) {
        // 3.6.1 check successfully save data to file json
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));
            pw.println(data);
            pw.close();
        } catch (IOException e) {
            //3.6.2 insert control.logs(event, status, note) values ("Load from Source to File", "fail", "e.message");
            writeLog(e.getMessage());
        }
    }

    // 2.5.3 parse data to csv file
    public void parseToCSVFile(String path) {
        JsonParser jsonParser = new JsonParser();
        // 3.7.1 check successfully parse data
        try {
            Object object = jsonParser.parse(new FileReader(path));
            JsonArray jsonArray = (JsonArray) object;
            String[] header = {"title", "image", "category", "description", "content", "author", "tags", "created_at", "updated_at", "created_by", "updated_by"};
            CSVWriter csvWriter = new CSVWriter(new FileWriter("Load_from_Source_To_File/src/main/java/data/" + getFileNameByCurrentDateFormat(new Date())));
            csvWriter.writeNext(header);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < jsonArray.size(); i++) {
                String[] data = new String[11];
                data[0] = jsonArray.get(i).getAsJsonObject().get("title").getAsString();
                data[1] = jsonArray.get(i).getAsJsonObject().get("image").getAsString();
                data[2] = jsonArray.get(i).getAsJsonObject().get("category").getAsString();
                data[3] = jsonArray.get(i).getAsJsonObject().get("description").getAsString();
                data[4] = jsonArray.get(i).getAsJsonObject().get("content").getAsString();
                data[5] = jsonArray.get(i).getAsJsonObject().get("author").getAsString();
                data[6] = jsonArray.get(i).getAsJsonObject().get("tags").toString();
                data[7] = formatter.format(new Date());
                data[8] = formatter.format(new Date());
                data[9] = "admin";
                data[10] = "admin";
                csvWriter.writeNext(data);
            }
            csvWriter.close();

        } catch (Exception e) {
            //3.7.1.1 insert control.logs(event, status, note) values ("Load from Source to File", "fail", "e.message");
            writeLog(e.getMessage());
        }
    }

    public String getFileNameByCurrentDateFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
        String dateStr = formatter.format(date);
        return "VNEXPRESS_" + dateStr + ".csv";
    }


    public void run() {
        // 3.1 connect to database
         connection = new GetConnection(url, user, password);
        try {
            //3.1.1 check connect successfully
            Connection connect = connection.getConnection();
            if (connect == null) {
                //3.1.2 insert control.logs(event, status, note) values ("Load from Source to File", "fail", "connect to control database failed");
                writeLog("connect to control database failed");
                // kết thúc
                return;
            }
            // success
            //3.2 insert control.logs(event, status) values ("Load from Source to File", "in process");
            insertLogProcess();
            Statement statement = connect.createStatement();
            // 3.3 insert source path to table data_file_configs
            String sourcePath = "https://vnexpress.net/thoi-su";
            insertSourcePath(sourcePath);
            // 3.4 insert news row to table data_files with id of data_file_configs
            insertDataFiles();
            // 3.5 crawl data from source
            String data = crawlData(sourcePath);
            // 3.6 save data to file json
            saveDataToFileJson(data, "Load_from_Source_To_File/src/main/java/data/data.json");
            // 3.7 parse data to csv file
            parseToCSVFile("Load_from_Source_To_File/src/main/java/data/data.json");
            // 3.8 update status to table data_files
            udpateStatusDataFiles();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2.6 update status to table data_files
    private void udpateStatusDataFiles() {
        Connection connect = connection.getConnection();
        String query = "UPDATE control.data_files SET status = ?, note = ?, updated_at = current_date(), updated_by = ? WHERE id = ?";
        // 3.8.1 check update successfully status to table data_files
        try {
            PreparedStatement pre = connect.prepareStatement(query);
            pre.setString(1,moduleLoad);
            pre.setString(2, moduleSuccess);
            pre.setString(3, "root");
            pre.setInt(4, 1);
            pre.executeUpdate();
            pre.close();
        } catch (Exception e) {
            // 3.8.1.1 insert control.logs(event, status, note) values ("Load from Source to File", "fail", "e.message");
           writeLog(e.getMessage());
        }
    }

    public void insertLogProcess() {
        Connection connect = connection.getConnection();
        String query = "INSERT INTO control.logs(event, status) VALUES (?, ?)";
        try {
            PreparedStatement pre = connect.prepareStatement(query);
            pre.setString(1, moduleLoad);
            pre.setString(2, moduleProcess);
            pre.executeUpdate();
            pre.close();
        } catch (SQLException e) {
            writeLog(e.getMessage());
        }
    }

    // 2.3 add source path to table data_file_configs
    private void insertSourcePath(String sourcePath) {
        String csvFile = "VNExpress" + new SimpleDateFormat("dd_MM_yyyy").format(new Date());
        String location = "D:\\data_warehouse\\DW_2023_T4_Nhom6\\Load_from_Source_To_File\\src\\main\\java\\data\\" + csvFile + ".csv";
        Connection connect = connection.getConnection();
        String query = "INSERT INTO control.data_file_configs(source_path, location, status, note, created_at, updated_at, created_by, updated_by)" +
                "VALUES (?, ?, ?, ?, current_date(), current_date(), ?, ?)";
        // 3.3.1 check insert successfully source path to table data_file_configs
        try {
            PreparedStatement pre = connect.prepareStatement(query);
            pre.setString(1, sourcePath);
            pre.setString(2, location);
            pre.setString(3, moduleSuccess);
            pre.setString(4, "Data import success");
            pre.setString(5, "root");
            pre.setString(6, "root");
            pre.executeUpdate();
            pre.close();
        } catch (Exception e) {
            // 3.3.1.1 insert control.logs(event, status, note) values ("Load from Source to File", "fail", "e.message");
            writeLog(e.getMessage());
        }
    }

    // 2.4 add news row to table data_files with id of data_file_configs
    public void insertDataFiles() {
        connection = new GetConnection(url, user, password);
        String query = "INSERT INTO control.data_files(name, row_count, status, note, created_at, updated_at, created_by, updated_by)" +
                "VALUES (?, ?, ?, ?, current_date(), current_date(), ?, ?)";

        // 3.4.1 check insert successfully source path to table data_file_configs
        try {
            PreparedStatement pre = connection.getConnection().prepareStatement(query);
            pre.setString(1, csv + new SimpleDateFormat("dd_MM_yyyy").format(new Date()));
            pre.setInt(2, countCSVLines(loadFilePath() + csv + new SimpleDateFormat("dd_MM_yyyy").format(new Date()) + ".csv"));
            pre.setString(3, moduleLoad);
            pre.setString(4, moduleSuccess);
            pre.setString(5, "root");
            pre.setString(6, "root");
            pre.executeUpdate();
            pre.close();
        } catch (Exception e) {
            // 3.4.1.1 insert control.logs(event, status, note) values ("Load from Source to File", "fail", "e.message");
            writeLog(e.getMessage());
        }
    }

    public int countCSVLines(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        int lineCount = 0;
        while (reader.readLine() != null) {
            lineCount++;
        }
        return lineCount;
    }

    public void writeLog(String note) {
        Connection connect = connection.getConnection();
        String query = "INSERT control.Logs(event, status, note) VALUES (?, ?, ?)";
        try {
            PreparedStatement pre = connect.prepareStatement(query);
            pre.setString(1, moduleLoad);
            pre.setString(2, "fail");
            pre.setString(3, note);
            pre.executeUpdate();
            pre.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String loadFilePath() {

        String filePath = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        if (filePath == null || filePath.isEmpty()) {
            connection.writeLogToFile("Load_from_Source_To_File/src/main/java/log.txt", "error", "Invalid file path");
        } else {
            filePath = csvFilePath;
        }
        return filePath;
    }

    // 1. run main
    public static void main(String[] args) {
        ConfigReader configReader = new ConfigReader();
        // 1 load config
        LoadSourceToFile loadSourceToFile = new LoadSourceToFile(configReader);
        // 2 run
        loadSourceToFile.run();
    }
}
