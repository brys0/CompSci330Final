package org.uwgb.compsci330.frontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.common.model.response.message.SafeMessage;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;
import org.uwgb.compsci330.common.websocket.model.out.message.MessageCreatedEvent;
import org.uwgb.compsci330.frontend.controller.base.CommonController;

import java.io.IOException;

public class ChatController extends CommonController {
    @FXML
    private ImageView restoreButtonImage;

    protected ChatController(Parent parent, Stage stage, Client client) {
        super(parent, stage, client);

        client.getWs().bus.on(OutboundEventType.HELLO, e -> {
            System.out.println("Got hello event from server.");
        });

        client.getWs().bus.on(OutboundEventType.MESSAGE_CREATED, e -> {
            final MessageCreatedEvent event = (MessageCreatedEvent) e;
            final SafeMessage message = event.getPayload();

            System.out.printf("Got message: %s\n", message);
        });
        client.getWs().connect();
    }

    @FXML
    void chatFoldButtonPress(ActionEvent event) {

    }

    @FXML
    void settingsButtonPress(ActionEvent event) throws IOException {
        SettingsController.open(stage, client);
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
