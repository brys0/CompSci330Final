package org.uwgb.compsci330.frontend;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.stage.StageStyle;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.ClientConfig;
import org.uwgb.compsci330.frontend.client.ClientSingleton;
import org.uwgb.compsci330.frontend.controller.SignInController;
import org.uwgb.compsci330.frontend.util.FXMLUtils;

import java.io.IOException;

public class FXApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {


        stage.setTitle("NAME HERE");
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);

        SignInController signInController = new SignInController(null, stage, ClientSingleton.getInstance().getClient());
        var view = FXMLUtils.load("/xml/pages/signIn/signIn.fxml", signInController);


        Scene scene = new Scene(view.root());
        scene.getStylesheets().add(
                getClass().getResource("/css/style.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
