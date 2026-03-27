package org.uwgb.compsci330.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.frontend.client.ClientSingleton;

import java.io.IOException;

public class SignInController {

    private Stage stage;
    public void setStage(Stage stage) { this.stage = stage; }

    private final Client client = ClientSingleton.getInstance().getClient();

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

        try {
            signInButton.setDisable(true);
            client.login(username, password);

            FXMLLoader chatLoader = new FXMLLoader(
                    getClass().getResource(
                            "/xml/pages/chat/chat.fxml"
                    )
            );

            Parent chatFXML = chatLoader.load();

            ChatController chatController = chatLoader.getController();
            chatController.setStage(stage);

            stage.getScene().setRoot(chatFXML);

            stage.setResizable(true);
            stage.setFullScreen(true);

        } catch (IOException e) {
            throw new RuntimeException(e);

        } finally {
            signInButton.setDisable(false);

        }

    }

    @FXML
    void signUp(ActionEvent event) throws IOException {

        FXMLLoader signUpLoader = new FXMLLoader(
                getClass().getResource(
                        "/xml/pages/signUp/signUp.fxml"
                )
        );

        Parent signUpFXML = signUpLoader.load();

        SignUpController signUpController = signUpLoader.getController();
        signUpController.setStage(stage);

        stage.getScene().setRoot(signUpFXML);

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
