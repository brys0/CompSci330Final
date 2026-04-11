package org.uwgb.compsci330.frontend.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.entity.user.SelfUser;
import org.uwgb.compsci330.frontend.controller.base.CommonController;

import java.io.IOException;

public class SignUpController extends CommonController {
    public SignUpController(Parent parent, Stage stage, Client client) {
        super(parent, stage, client);
    }

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField password1;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField username;

    @FXML
    void closeButtonPress(ActionEvent event) {
        stage.close();
    }

    @FXML
    void minimizeButtonPress(ActionEvent event) {
        stage.setIconified(true);
    }

    @FXML
    void confirmSignUp(ActionEvent event) {
        final String username = this.username.getText();
        final String password = this.password.getText();
        final String password1 = this.password1.getText();

        Task<SelfUser> signUpTask = new Task<>() {
            @Override
            protected SelfUser call() throws IOException {
                return client.register(username, password);
            }
        };

        signUpTask.setOnSucceeded(e -> {
            signUpButton.setDisable(false);

            try {
                this.navigateToFullscreen("/xml/pages/chat/chat.fxml", new ChatController(this.parent, this.stage, this.client));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        signUpTask.setOnFailed(e -> {
            Throwable ex = signUpTask.getException();
            ex.printStackTrace(); // temporary, replace with proper error display

            signUpButton.setDisable(false);
        });

        if(password.equals(password1)) {
            signUpButton.setDisable(true);
            new Thread(signUpTask).start();
        }
    }

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    void windowBarMouseDragged(MouseEvent event) {
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    void windowBarMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

}
