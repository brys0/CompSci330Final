package org.uwgb.compsci330.frontend;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class FXApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("NAME HERE");

        Parent settingsFXML = FXMLLoader.load(
                getClass().getResource(
                        "/xml/pages/settings/settings.fxml"
                )
        );
        Scene settings = new Scene(settingsFXML);

        Parent chatFXML = FXMLLoader.load(
                getClass().getResource(
                        "/xml/pages/chat/chat.fxml"
                )
        );
        Scene chat = new Scene(chatFXML);

        Parent loginFXML = FXMLLoader.load(
                getClass().getResource(
                        "/xml/pages/login/login.fxml"
                )
        );
        Scene login = new Scene(loginFXML);

        stage.setScene(login);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
