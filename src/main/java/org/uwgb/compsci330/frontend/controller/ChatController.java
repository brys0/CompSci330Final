package org.uwgb.compsci330.frontend.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.entity.message.Message;
import org.uwgb.compsci330.client_sdk.entity.relationship.Relationship;
import org.uwgb.compsci330.client_sdk.entity.user.User;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;
import org.uwgb.compsci330.frontend.controller.base.CommonController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatController extends CommonController {
    @FXML private ImageView restoreButtonImage;
    @FXML private ListView<Relationship> friendsList;
    @FXML private ListView<Message> messageList;
    private Relationship selectedRelationship = null;


    protected ChatController(Parent parent, Stage stage, Client client) {
        super(parent, stage, client);
    }

    @FXML
    public void initialize() {
        setupFriendsList();
        setupMessageList();
        setupEventListeners();
        client.getWs().connect();
    }

    private void setupFriendsList() {
        // custom cell factory to show friend username
        friendsList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Relationship relationship, boolean empty) {
                super.updateItem(relationship, empty);
                if (empty || relationship == null) {
                    setText(null);
                } else {
                    final User friend = relationship.getUser();

                    setText(String.format("%s: %s", friend.getUsername(), friend.getStatus()));
                }
            }
        });

        // on friend selected, load messages
        friendsList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedRelationship = newVal;
                loadMessages(newVal);
            }
        });

        // load initial friends list
        loadFriends();
    }

    private void setupMessageList() {
        messageList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Message item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getSender().getUsername() + ": " + item.getContent());
                }
            }
        });
    }

    private void setupEventListeners() {
        client.getWs().bus.on(OutboundEventType.HELLO, e -> loadFriends());

        AtomicInteger reconnectTest = new AtomicInteger();


        client.getWs().bus.on(OutboundEventType.MESSAGE_CREATED, e -> {
            System.out.println("Message create event received.");
            reconnectTest.getAndIncrement();


            Message message = (Message) e;
                Platform.runLater(() -> {
                    try {
                        ((Message) e).getConversation().createMessage(String.format("echo: %s", ((Message) e).getContent()));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    messageList.getItems().add(message);
                    messageList.scrollTo(messageList.getItems().size() - 1);
                });
        });

        client.getWs().bus.on(OutboundEventType.STATUS, e -> {
            System.out.println("STATUS event received in UI: " + e);
            System.out.println("Type: " + e.getClass().getName());
            Platform.runLater(() -> friendsList.refresh());
        });

        client.getWs().bus.on(OutboundEventType.RELATIONSHIP_CREATED, e -> {
            Relationship relationship = (Relationship) e;

            Platform.runLater(() -> friendsList.getItems().add(relationship));
        });

        client.getWs().bus.on(OutboundEventType.RELATIONSHIP_DELETED, e -> {
            String id = (String) e;
            Platform.runLater(() ->
                    friendsList.getItems().removeIf(r -> r.getId().equals(id))
            );
        });
    }

    private void loadFriends() {
        // fetch from API on virtual thread, update UI on JavaFX thread
        Thread.ofVirtual().start(() -> {
            List<Relationship> relationships = null;
            try {
                relationships = client.getRelationships().fetchRelationships();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            List<Relationship> finalRelationships = relationships;
            Platform.runLater(() -> {
                friendsList.getItems().clear();
                friendsList.getItems().addAll(finalRelationships);
            });
        });
    }

    private void loadMessages(Relationship relationship) {
//        Thread.ofVirtual().start(() -> {
//            List<Message> messages = client.getMessageApi()
//                    .getMessages(relationship.getConversationId());
//            Platform.runLater(() -> {
//                messageList.getItems().clear();
//                messageList.getItems().addAll(messages);
//                messageList.scrollTo(messageList.getItems().size() - 1);
//            });
//        });
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
