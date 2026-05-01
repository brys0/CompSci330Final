package org.uwgb.compsci330.frontend.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.entity.message.Message;
import org.uwgb.compsci330.client_sdk.entity.relationship.Relationship;
import org.uwgb.compsci330.client_sdk.entity.user.User;
import org.uwgb.compsci330.common.model.response.message.MessageType;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;
import org.uwgb.compsci330.frontend.controller.base.CommonController;

import java.awt.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ChatController extends CommonController {
    private static int MAX_FETCH_SIZE = 100;
    public TextArea messageTextArea;
    public Button sendButton;

    @FXML
    private ImageView restoreButtonImage;
    @FXML
    private ListView<Relationship> friendsList;
    @FXML
    private ListView<Message> messageList;
    private Relationship selectedRelationship = null;
    private boolean isLoadingMessages = false;
    private boolean hasMoreMessages = true;

    protected ChatController(Parent parent, Stage stage, Client client) {
        super(parent, stage, client);
    }

    @FXML
    public void initialize() {
        setupFriendsList();
        setupMessageList();
        setupEventListeners();
        client.getWs().connect();

        // You can add "create" a relationship like this
        // client
        // .getRelationships()
        // .createRelationship("some username")
        // Delete them like this
        // client
        // .getRelationships()
        // .deleteRelationship(username, relationshipId);
    }

    private void setupFriendsList() {
        // custom cell factory to show friend username
        friendsList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Relationship relationship, boolean empty) {
                super.updateItem(relationship, empty);
                if (empty || relationship == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(null);
                    final User friend = relationship.getUser();

                    setText(String.format("%s: %s", friend.getUsername(), friend.getStatus()));
                }
            }
        });

        // on friend selected, load messages
        friendsList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            messageList.getItems().clear();
            hasMoreMessages = true;

            if (newVal != null) {
                selectedRelationship = newVal;
                loadMessages(newVal, null);

                Platform.runLater(() -> {
                    messageList.scrollTo(messageList.getItems().size() - 1);
                });
            }
        });


        messageList.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin == null) return;
            ScrollBar scrollBar = (ScrollBar) messageList.lookup(".scroll-bar:vertical");
            if (scrollBar == null) return;

            scrollBar.valueProperty().addListener((o, oldVal, newVal) -> {
                // 1. Check isLoading immediately before doing ANYTHING
                if (isLoadingMessages || !hasMoreMessages) return;

                if (newVal.doubleValue() < 0.02) {
                    isLoadingMessages = true; // Set this IMMEDIATELY on the UI thread

                    Message oldest = messageList.getItems().getFirst();
                    String oldestId = oldest.getId();

                    Thread.ofVirtual().start(() -> {
                        try {
                            List<Message> older = selectedRelationship.getConversation()
                                    .fetchMessages(MAX_FETCH_SIZE, null, oldestId);

                            // 2. We are still on a background thread.
                            // We need to jump back to UI thread to modify the list.
                            Platform.runLater(() -> {
                                try {
                                    if (older.isEmpty()) {
                                        hasMoreMessages = false;
                                    } else {
                                        messageList.getItems().addAll(0, older);

                                        messageList.getItems().sort(Comparator.comparing(Message::getCreatedAt));

                                        messageList.scrollTo(older.size());
                                    }
                                } finally {
                                    // 3. ONLY allow more loading AFTER the UI has finished rendering
                                    isLoadingMessages = false;
                                }
                            });
                        } catch (IOException e) {
                            Platform.runLater(() -> isLoadingMessages = false);
                            e.printStackTrace();
                        }
                    });
                }
            });
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
                    setText(formatMessage(item));
                }
            }
        });
    }

    private String formatMessage(Message item) {
        final User user = item.getSender();
        final boolean isSystem = item.getType() == MessageType.SYSTEM;
        final String formatted;
        if (isSystem) {
            formatted = item.getContent();
        } else {
            final String username = user == null ? "Unknown User" : user.getUsername();
            formatted = String.format("%s: %s", username, item.getContent());
        }
        return formatted;
    }

    private void setupEventListeners() {
        client.getWs().bus.on(OutboundEventType.MESSAGE_CREATED, e -> {
            System.out.println("Message create event received.");

            Message message = (Message) e;

            if (!Objects.equals(message.getConversation().getConversationId(), selectedRelationship.getConversation().getConversationId())) return;

            Platform.runLater(() -> {
                messageList.getItems().add(message);
                messageList.scrollTo(messageList.getItems().size() - 1);
            });
        });

        client.getWs().bus.on(OutboundEventType.MESSAGE_DELETED, e -> {
            Platform.runLater(() -> {
                messageList
                        .getItems()
                        .removeIf(m -> Objects.equals(m.getId(), e));
            });
        });

        client.getWs().bus.on(OutboundEventType.STATUS, e -> {
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

    private void loadMessages(Relationship relationship, String beforeId) {
        Thread.ofVirtual().start(() -> {
            try {
                List<Message> messages = relationship.getConversation()
                        .fetchMessages(50, null, beforeId).reversed();

                Platform.runLater(() -> {
                    messageList.getItems().clear();

                    for (var message : messages) {
                        messageList.getItems().add(message);
                    }

                    messageList.scrollTo(messageList.getItems().size() - 1);
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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

    public void addFriendButtonPress(ActionEvent actionEvent) throws IOException {
        FriendController.open(stage, client);
    }

    public void sendButtonPress(ActionEvent actionEvent) {
        final String message = messageTextArea.getText();

        Thread.ofVirtual().start(() -> {
            if (selectedRelationship != null) {
                try {
                    selectedRelationship.getConversation().createMessage(message);

                    Platform.runLater(() -> {
                        sendButton.setDisable(false);
                        messageTextArea.clear();
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        sendButton.setDisable(true);
    }
}
