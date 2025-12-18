package sample.model;

import java.time.LocalDate;

public class BlogPost {
    private String title;
    private String content;
    private String category;
    private LocalDate date;

    public BlogPost(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.date = LocalDate.now();
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setCategory(String category) { this.category = category; }
}
