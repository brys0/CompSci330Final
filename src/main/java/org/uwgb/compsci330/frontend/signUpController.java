package org.uwgb.compsci330.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.frontend.client.ClientSingleton;

public class signUpController {
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
    void confirmSignUp(ActionEvent event) {

        //client.registerUser();
    }

}
