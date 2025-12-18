package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.BlogPost;
import sample.model.BlogRepository;

import java.io.IOException;

public class MainController {

    @FXML
    private ListView<BlogPost> postList;
    @FXML
    private Label titleLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private Label contentLabel;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button saveButton;
    @FXML
    private TextField searchField;

    private BlogRepository repo = new BlogRepository();

    @FXML
    public void initialize() {

        repo.loadFromFile("blog_posts.txt");
        postList.setItems(repo.getPosts());


        postList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(BlogPost post, boolean empty) {
                super.updateItem(post, empty);
                setText(empty || post == null ? null : post.getTitle());
            }
        });


        postList.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) showPostDetails(newSel);
        });


        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldText, newText) -> filterPosts(newText));
        }


        if (!repo.getPosts().isEmpty()) postList.getSelectionModel().select(0);
    }

    private void showPostDetails(BlogPost post) {
        titleLabel.setText(post.getTitle());
        categoryLabel.setText(post.getCategory());
        contentLabel.setText(post.getContent());
    }

    private void filterPosts(String filter) {
        if (filter == null || filter.isEmpty()) {
            postList.setItems(repo.getPosts());
        } else {
            var filtered = repo.getPosts().filtered(post ->
                    post.getTitle().toLowerCase().contains(filter.toLowerCase())
            );
            postList.setItems(filtered);
            if (!filtered.isEmpty()) postList.getSelectionModel().select(0);
        }
    }

    @FXML
    private void onAddPost() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editor_view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            EditorController controller = loader.getController();
            controller.setPost(null);
            stage.showAndWait();

            BlogPost newPost = controller.getPost();
            if (newPost != null) {
                repo.addPost(newPost);
                postList.getSelectionModel().select(newPost);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onEditPost() {
        BlogPost selected = postList.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editor_view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            EditorController controller = loader.getController();
            controller.setPost(selected);
            stage.showAndWait();

            postList.refresh();
            showPostDetails(selected);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onDeletePost() {
        BlogPost selected = postList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            repo.removePost(selected);

            if (!repo.getPosts().isEmpty()) {
                postList.getSelectionModel().select(0);
            } else {
                titleLabel.setText("");
                categoryLabel.setText("");
                contentLabel.setText("");
            }
        }
    }

    @FXML
    private void onSaveToFile() {
        repo.saveToFile("blog_posts.txt");
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Posts saved to blog_posts.txt");
        alert.showAndWait();
    }
}
