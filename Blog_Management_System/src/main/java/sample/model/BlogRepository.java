package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class BlogRepository {

    private ObservableList<BlogPost> posts = FXCollections.observableArrayList();

    public ObservableList<BlogPost> getPosts() {
        return posts;
    }

    public void addPost(BlogPost post) {
        posts.add(post);
    }

    public void removePost(BlogPost post) {
        posts.remove(post);
    }

    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (BlogPost post : posts) {
                writer.println(post.getTitle());
                writer.println(post.getCategory());
                writer.println(post.getContent().replace("\n", "\\n"));
                writer.println("===");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadFromFile(String filename) {
        posts.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String title = null, category = null;
            StringBuilder contentBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.equals("===")) {
                    if (title != null && category != null) {
                        posts.add(new BlogPost(title, category, contentBuilder.toString().replace("\\n", "\n")));
                    }
                    title = category = null;
                    contentBuilder.setLength(0);
                } else if (title == null) {
                    title = line;
                } else if (category == null) {
                    category = line;
                } else {
                    if (contentBuilder.length() > 0) contentBuilder.append("\n");
                    contentBuilder.append(line);
                }
            }


            if (title != null && category != null) {
                posts.add(new BlogPost(title, category, contentBuilder.toString().replace("\\n", "\n")));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
