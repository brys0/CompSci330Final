package org.uwgb.compsci330.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.frontend.client.ClientSingleton;

import java.io.IOException;

public class loginController {
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
    void signIn(ActionEvent event) throws IOException {
        final String username = this.username.getText();
        final String password = this.password.getText();

        try {
            signInButton.setDisable(true);
            client.login(username, password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            signInButton.setDisable(false);
        }

    }

    @FXML
    void signUp(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        Parent signUpFXML = new FXMLLoader(
                getClass().getResource(
                        "/xml/pages/login/signUp.fxml"
                )
        ).load();

        Scene signUp = new Scene(signUpFXML);

        stage.setScene(signUp);

    }

}
