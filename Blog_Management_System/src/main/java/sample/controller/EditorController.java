    package sample.controller;

    import javafx.fxml.FXML;
    import javafx.scene.control.TextArea;
    import javafx.scene.control.TextField;
    import javafx.stage.Stage;
    import sample.model.BlogPost;
    import sample.model.BlogRepository;

    public class EditorController {

        @FXML
        private TextField titleField;

        @FXML
        private TextField categoryField;

        @FXML
        private TextArea contentField;

        private BlogPost post;
        private BlogRepository repo;

        public void setRepository(BlogRepository repo) {
            this.repo = repo;
        }

        public void setPost(BlogPost post) {
            this.post = post;

            if (post != null) {
                titleField.setText(post.getTitle());
                categoryField.setText(post.getCategory());
                contentField.setText(post.getContent());
            }
        }

        @FXML
        public void onSave() {
            if (post == null) {
                post = new BlogPost(
                        titleField.getText(),
                        contentField.getText(),
                        categoryField.getText()
                );
                repo.addPost(post);
            } else {
                post.setTitle(titleField.getText());
                post.setCategory(categoryField.getText());
                post.setContent(contentField.getText());
            }

            ((Stage) titleField.getScene().getWindow()).close();
        }

        @FXML
        public void onCancel() {
            Stage stage = (Stage) titleField.getScene().getWindow();
            stage.close();
        }

    }
