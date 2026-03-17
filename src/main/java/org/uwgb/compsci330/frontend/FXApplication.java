package org.uwgb.compsci330.frontend;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class FXApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("NAME HERE");

        Parent loginFXML = new FXMLLoader(
                getClass().getResource(
                        "/xml/pages/login/login.fxml"
                )
        ).load();
        Scene login = new Scene(loginFXML);

        stage.setScene(login);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
