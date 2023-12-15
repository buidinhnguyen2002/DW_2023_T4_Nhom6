package model;

import java.util.Date;

public class PresentNewsArticle {

    private String id;
    private String title;
    private String description;
    private String content;
    private String image;
    private String category;
    private String authorName;
    private Date date;
    private String tags;

    public PresentNewsArticle(String id, String title, String description, String content, String image,
                              String category, String authorName, Date date, String tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.content = content;
        this.image = image;
        this.category = category;
        this.authorName = authorName;
        this.date = date;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "PresentNewsArticle{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", category='" + category + '\'' +
                ", authorName='" + authorName + '\'' +
                ", date=" + date +
                ", tags='" + tags + '\'' +
                '}';
    }
}
