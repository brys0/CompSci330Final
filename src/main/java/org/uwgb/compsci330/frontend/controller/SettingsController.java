package org.uwgb.compsci330.frontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.frontend.controller.base.CommonController;
import org.uwgb.compsci330.frontend.util.FXMLUtils;

import java.io.IOException;

public class SettingsController extends CommonController {
    private final Stage ownerStage;

    public SettingsController(Parent parent, Stage stage, Stage ownerStage, Client client) {
        super(parent, stage, client);
        this.ownerStage = ownerStage;
    }

    public static void open(Stage owner, Client client) throws IOException {
        Stage settingsStage = new Stage();
        settingsStage.setTitle("Settings");
        settingsStage.initStyle(StageStyle.UNDECORATED);
        settingsStage.initOwner(owner);

        SettingsController controller = new SettingsController(null, settingsStage, owner, client);
        FXMLUtils.LoadedView<SettingsController> view = FXMLUtils.load(
                "/xml/pages/settings/settings.fxml",
                controller
        );

        Scene scene = new Scene(view.root());

        settingsStage.setScene(scene);
        settingsStage.show();
    }

    @FXML
    void signOutButtonPress(ActionEvent event) throws IOException {
        stage.close(); // close settings
        ownerStage.setResizable(false);
        ownerStage.setFullScreen(false);
        navigateTo(ownerStage, "/xml/pages/signIn/signIn.fxml",
                new SignInController(null, ownerStage, client));
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