package org.uwgb.compsci330.frontend.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.entity.message.Message;
import org.uwgb.compsci330.client_sdk.entity.relationship.Relationship;
import org.uwgb.compsci330.client_sdk.entity.user.User;
import org.uwgb.compsci330.common.model.response.message.MessageType;
import org.uwgb.compsci330.common.model.response.relationship.RelationshipStatus;
import org.uwgb.compsci330.common.model.response.user.UserStatus;
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
    public Label friendName;

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
        setupEventListeners();
        client.getWs().connect();

        messageList.setCellFactory(m -> new MessageViewCell());
        // You can add "create" a relationship like this
        // client
        // .getRelationships()
        // .createRelationship("some username")
        // Delete them like this
        // client
        // .getRelationships()
        // .deleteRelationship(username, relationshipId);

        messageTextArea.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() != KeyCode.ENTER) return;
            if (keyEvent.isShiftDown()) {
                messageTextArea.appendText("\n");
                return;
            };

            sendButtonPress(null);
        });
    }

    private void setupFriendsList() {
        // custom cell factory to show friend username
        friendsList.setCellFactory(lv -> new FriendListViewCell());

        // on friend selected, load messages
        friendsList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            messageList.getItems().clear();
            hasMoreMessages = true;

            if (newVal != null) {
                selectedRelationship = newVal;
                friendName.setText(newVal.getUser().getUsername());
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

                if (newVal.doubleValue() < 0.02 && messageList.getItems().size() > 20) {
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
                relationships = client
                        .getRelationships()
                        .fetchRelationships()
                        .stream()
                        .filter(r -> r.getStatus() == RelationshipStatus.ACCEPTED)
                        .toList();
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

    private class FriendListViewCell extends ListCell<Relationship> {
        public static Image onlineImage = new Image("/images/chat_online.png");
        public static Image offlineImage = new Image("/images/chat_offline.png");

        final VBox relationshipView = new VBox();
        final HBox usernameAndStatus = new HBox();
        final HBox statusViewBox = new HBox();
        final Label username = new Label();
        final ImageView statusView = new ImageView();
        final Label lastMessage = new Label();


        public FriendListViewCell() {
            relationshipView.setSpacing(4);

            statusView.setFitWidth(12);
            statusView.setFitHeight(12);


            HBox.setHgrow(relationshipView, Priority.ALWAYS);
            HBox.setHgrow(usernameAndStatus, Priority.ALWAYS);
            HBox.setHgrow(statusViewBox, Priority.ALWAYS);

            usernameAndStatus.setAlignment(Pos.CENTER_LEFT);
            statusViewBox.setAlignment(Pos.CENTER_RIGHT);

            username.getStyleClass().add("primaryTextColor");

            statusViewBox.getChildren().add(statusView);
            usernameAndStatus
                    .getChildren()
                    .addAll(username, statusViewBox);

            relationshipView
                    .getChildren()
                    .addAll(usernameAndStatus);
        }

        @Override
        protected void updateItem(Relationship relationship, boolean empty) {
            super.updateItem(relationship, empty);

            if (relationship == null || empty) {
                setGraphic(null);
                setText(null);
            } else {
                setText(null);

                var user = relationship.getUser();
//                var messages = relationship.getConversation().getMessages();

                username.setText(user.getUsername());
                statusView.setImage(user.getStatus() == UserStatus.ONLINE ? onlineImage : offlineImage);
//                if (!messages.isEmpty()) {
//                    lastMessage.setText(messages.getFirst().getContent());
//                    if (!relationshipView.getChildren().contains(lastMessage)) {
//                        relationshipView.getChildren().add(lastMessage);
//                    }
//                } else {
//                    relationshipView.getChildren().remove(lastMessage);
//                }

                setGraphic(relationshipView);
            }
        }
    }
    private class MessageViewCell extends ListCell<Message> {
        final VBox messageBox = new VBox();
        final Label username = new Label();
        final Label text = new Label();

        public MessageViewCell() {
            messageBox.setSpacing(4);
            username.getStyleClass().add("message-user");
            text.getStyleClass().add("message-text");
            text.setWrapText(true);

            // Ensure the VBox doesn't grow wider than the cell
            messageBox.setMaxWidth(Control.USE_PREF_SIZE);

            listViewProperty().addListener((obs, oldList, newList) -> {
                if (newList != null) {
                    // Bind the VBox to the list width
                    // Increase subtraction to 40 to account for padding + scrollbar
                    messageBox.prefWidthProperty().bind(newList.widthProperty().subtract(40));

                    // Explicitly bind the text label width as well
                    text.prefWidthProperty().bind(messageBox.prefWidthProperty());
                }
            });

            messageBox.getChildren().addAll(username, text);
        }

        @Override
        protected void updateItem(Message message, boolean empty) {
            super.updateItem(message, empty);

            if (empty || message == null) {
                setGraphic(null);
                setText(null);
            } else {
                if (message.getSender() != null) {
                    username.setText(message.getSender().getUsername());
                    username.setManaged(true);
                    username.setVisible(true);
                } else {
                    username.setManaged(false);
                    username.setVisible(false);
                }

                text.setText(message.getContent());
                setGraphic(messageBox);
                setText(null);
            }
        }
    }
}
