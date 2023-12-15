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
import java.util.Random;

public class CrawDataFromSource {
    // check if not exist, create folder
    public static boolean isFolderExist(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }
        return file.mkdirs();
    }

    public String crawlData(String url) throws IOException {
        // check connection
        if (url == null || url.isEmpty()) {
            return "URL is empty";
        }
        Document doc = Jsoup.connect(url).get();
        String category = doc.select("div.title-folder > h1 > a").text();
        Elements tags = doc.select("ul.ul-nav-folder > li");
        System.out.println(tags.size());
        Elements elements = doc.select("article.item-news.item-news-common.thumb-left");
        System.out.println(elements.size());
        com.google.gson.JsonArray jsonArray = new JsonArray();
        for (Element element : elements) {
            String id = category + "_" + elements.indexOf(element);
            String title = element.select("h3.title-news > a").text();
            String image = element.select("picture > img").attr("src");
            String realImg = "";
            if (image.contains("base64")) {
                realImg = "http://graph.vnecdn.net/" + image.split(",")[1];
            } else {
                realImg = image;
            }
            System.out.println(realImg);
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
            jsonObject.addProperty("id", id);
            jsonObject.addProperty("title", title);
            jsonObject.addProperty("image", realImg);
            jsonObject.addProperty("category", category);
            jsonObject.addProperty("link", link);
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

        Gson gson = new Gson();
        return gson.toJson(jsonArray);
    }
    public void saveDataToFile(String data, String path) {
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
    public void parseToCSVFile(String path) {
        // parse data to csv file
        JsonParser jsonParser = new JsonParser();
        try{
            Object object = jsonParser.parse(new FileReader(path));
            JsonArray jsonArray = (JsonArray) object;
            String[] header = {"id", "title", "image", "category", "link", "description", "content", "author", "tags","created_at","updated_at","created_by","updated_by","updated_by"};
            CSVWriter csvWriter = new CSVWriter(new FileWriter("src/main/java/data/"+getFileNameByCurrentDateFormat(new Date())));
            csvWriter.writeNext(header);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < jsonArray.size(); i++) {
                String[] data = new String[13];
                data[0] = jsonArray.get(i).getAsJsonObject().get("id").getAsString();
                data[1] = jsonArray.get(i).getAsJsonObject().get("title").getAsString();
                data[2] = jsonArray.get(i).getAsJsonObject().get("image").getAsString();
                data[3] = jsonArray.get(i).getAsJsonObject().get("category").getAsString();
                data[4] = jsonArray.get(i).getAsJsonObject().get("link").getAsString();
                data[5] = jsonArray.get(i).getAsJsonObject().get("description").getAsString();
                data[6] = jsonArray.get(i).getAsJsonObject().get("content").getAsString();
                data[7] = jsonArray.get(i).getAsJsonObject().get("author").getAsString();
                data[8] = jsonArray.get(i).getAsJsonObject().get("tags").toString();
                data[9] = formatter.format(new Date());
                data[10] = formatter.format(new Date());
                data[11] = "admin";
                data[12] = "admin";
                csvWriter.writeNext(data);
            }
            csvWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFileNameByCurrentDateFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
        String dateStr = formatter.format(date);
        return dateStr + ".csv";
    }



}
