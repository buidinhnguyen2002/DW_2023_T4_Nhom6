import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrawDataFromSource {
    // 2.5.1 crawl data from source
    public String crawlData(String url){
        //2.5.1.1 check connection
        if (url == null || url.isEmpty()) {

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
            String title = element.select("h2.title-news > a").attr("title");
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
            e.printStackTrace();
        }
        Gson gson = new Gson();
        return gson.toJson(jsonArray);
    }
    // 2.5.2 save data to file json
    public void saveDataToFileJson(String data, String path) {
        // 2.5.2.1 check successfully save data to file json
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));
            pw.println(data);
            pw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // 2.5.3 parse data to csv file
    public void parseToCSVFile(String path) {
        JsonParser jsonParser = new JsonParser();
        // 2.5.3.1 check successfully parse data
        try{
            Object object = jsonParser.parse(new FileReader(path));
            JsonArray jsonArray = (JsonArray) object;
            String[] header = { "title", "image", "category", "description", "content", "author", "tags","created_at","updated_at","created_by","updated_by"};
            CSVWriter csvWriter = new CSVWriter(new FileWriter("Load_from_Source_To_File/src/main/java/data/"+getFileNameByCurrentDateFormat(new Date())));
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
            e.printStackTrace();
        }
    }

    static String getFileNameByCurrentDateFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
        String dateStr = formatter.format(date);
        return "VNEXPRESS_"+dateStr + ".csv";
    }

    public static void main(String[] args) {
        CrawDataFromSource crawDataFromSource = new CrawDataFromSource();
        String data = crawDataFromSource.crawlData("https://vnexpress.net/thoi-su/giao-thong");
        crawDataFromSource.saveDataToFileJson(data, "Load_from_Source_To_File/src/main/java/data/data.json");
        crawDataFromSource.parseToCSVFile("Load_from_Source_To_File/src/main/java/data/data.json");
    }
}
