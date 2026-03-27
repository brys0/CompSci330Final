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

public class SignUpController {

    private Stage stage;
    public void setStage(Stage stage) { this.stage = stage; }

    private final Client client = ClientSingleton.getInstance().getClient();

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

        if(password.equals(password1)) {
            try {
                signUpButton.setDisable(true);
                client.register(username, password);

                FXMLLoader loginLoader = new FXMLLoader(
                        getClass().getResource(
                                "/xml/pages/signIn/signIn.fxml"
                        )
                );

                Parent loginFXML = loginLoader.load();

                SignInController signInController = loginLoader.getController();
                signInController.setStage(stage);

                stage.getScene().setRoot(loginFXML);

            } catch (IOException e) {
                throw new RuntimeException(e);

            } finally {
                signUpButton.setDisable(false);

            }
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
