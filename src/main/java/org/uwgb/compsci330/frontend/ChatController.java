package org.uwgb.compsci330.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ChatController {

    private Stage stage;
    public void setStage(Stage stage) { this.stage = stage; }

    @FXML
    private ImageView restoreButtonImage;

    @FXML
    void chatFoldButtonPress(ActionEvent event) {

    }

    @FXML
    void settingsButtonPress(ActionEvent event) throws IOException {

        Stage settingsStage = new Stage();
        settingsStage.setTitle("Settings");

        settingsStage.initStyle(StageStyle.UNDECORATED);
        settingsStage.initOwner(stage);

        FXMLLoader settingsLoader = new FXMLLoader(
                getClass().getResource(
                        "/xml/pages/settings/settings.fxml"
                )
        );

        Parent settingsFXML = settingsLoader.load();

        SettingsController settingsController = settingsLoader.getController();
        settingsController.setStage(settingsStage);
        settingsController.setRootStage(stage);

        Scene settings = new Scene(settingsFXML);

        settings.getStylesheets().add(
                getClass().getResource(
                        "/css/style.css"
                ).toExternalForm()
        );

        settingsStage.setScene(settings);

        settingsStage.show();
    }


    @FXML
    void closeButtonPress(ActionEvent event) {
        stage.close();
    }

    @FXML
    void minimizeButtonPress(ActionEvent event) {
        stage.setIconified(true);
    }


    boolean fullscreen;
    Image restoreImage = new Image("/images/restore.png");
    Image fullscreenImage = new Image("/images/fullscreen.png");

    @FXML
    void restoreButtonPress(ActionEvent event) {

        if (stage.isFullScreen()) {
            restoreButtonImage.setImage(fullscreenImage);
        } else {
            restoreButtonImage.setImage(restoreImage);
        }

        stage.setFullScreen(!stage.isFullScreen());
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
