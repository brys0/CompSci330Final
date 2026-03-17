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

import java.io.IOException;

public class loginController {

    @FXML
    private PasswordField password;

    @FXML
    private Button signInButton;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField username;

    @FXML
    void signIn(ActionEvent event) {

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
