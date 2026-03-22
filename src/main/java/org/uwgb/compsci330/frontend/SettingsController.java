package org.uwgb.compsci330.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    private Stage stage;
    public void setStage(Stage stage) { this.stage = stage; }

    private Stage rootStage;
    public void setRootStage(Stage rootStage) { this.rootStage = rootStage; }

    @FXML
    void signOutButtonPress(ActionEvent event) throws IOException {

        stage.close();

        rootStage.setResizable(false);
        rootStage.setFullScreen(false);

        FXMLLoader loginLoader = new FXMLLoader(
                getClass().getResource(
                        "/xml/pages/signIn/signIn.fxml"
                )
        );

        Parent loginFXML = loginLoader.load();

        SignInController signInController = loginLoader.getController();
        signInController.setStage(rootStage);

        rootStage.getScene().setRoot(loginFXML);
    }

    @FXML
    void closeButtonPress(ActionEvent event) {
        stage.close();
    }

    @FXML
    void minimizeButtonPress(ActionEvent event) {
        stage.setIconified(true);
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