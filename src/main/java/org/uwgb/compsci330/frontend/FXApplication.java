package org.uwgb.compsci330.frontend;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.stage.StageStyle;

import java.io.IOException;

public class FXApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        stage.setTitle("NAME HERE");

        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);

        FXMLLoader signInLoader  = new FXMLLoader(
                getClass().getResource(
                        "/xml/pages/signIn/signIn.fxml"
                )
        );

        Parent signInFXML = signInLoader.load();

        SignInController signInController = signInLoader.getController();
        signInController.setStage(stage);

        Scene root = new Scene(signInFXML);

        root.getStylesheets().add(
                getClass().getResource(
                        "/css/style.css"
                ).toExternalForm()
        );

        stage.setScene(root);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
