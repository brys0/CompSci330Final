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

public class SignInController extends CommonController {

    public SignInController(Parent parent, Stage stage, Client client) {
        super(parent, stage, client);
    }

    @FXML
    private PasswordField password;

    @FXML
    private Button signInButton;

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
    void signIn(ActionEvent event) throws IOException {
        final String username = this.username.getText();
        final String password = this.password.getText();


        Task<SelfUser> loginTask = new Task<>() {
            @Override
            protected SelfUser call() throws IOException {
                return client.login(username, password);
            }
        };

        loginTask.setOnSucceeded(e -> {
            signInButton.setDisable(false);

            try {
                this.navigateToFullscreen("/xml/pages/chat/chat.fxml", new ChatController(this.parent, this.stage, this.client));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        loginTask.setOnFailed(e -> {
            Throwable ex = loginTask.getException();
            ex.printStackTrace(); // temporary, replace with proper error display

            signInButton.setDisable(false);
        });

        signInButton.setDisable(true);
        new Thread(loginTask).start();
    }

    @FXML
    void signUp(ActionEvent event) throws IOException {
        this.navigateTo("/xml/pages/signUp/signUp.fxml", new SignUpController(this.parent, this.stage, this.client));
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
