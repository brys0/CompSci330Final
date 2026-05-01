package org.uwgb.compsci330.frontend.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.entity.relationship.Relationship;
import org.uwgb.compsci330.client_sdk.entity.user.User;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;
import org.uwgb.compsci330.frontend.controller.base.CommonController;
import org.uwgb.compsci330.frontend.util.FXMLUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class FriendController extends CommonController {
    private final Stage ownerStage;
    private final ObservableList<User> outgoingList = FXCollections.observableArrayList();
    private final ObservableList<User> incomingList = FXCollections.observableArrayList();

    public FriendController(Parent parent, Stage stage, Stage ownerStage, Client client) {
        super(parent, stage, client);
        this.ownerStage = ownerStage;
    }

    public static void open(Stage owner, Client client) throws IOException {
        Stage settingsStage = new Stage();
        settingsStage.setTitle("Friends");
        settingsStage.initStyle(StageStyle.DECORATED);
        settingsStage.initOwner(owner);

        FriendController controller = new FriendController(null, settingsStage, owner, client);
        FXMLUtils.LoadedView<FriendController> view = FXMLUtils.load(
                "/xml/pages/friends/friend_popup.fxml",
                controller,
                false);

        Scene scene = new Scene(view.root());

        settingsStage.setScene(scene);
        settingsStage.show();
    }

    @FXML
    private TextField addFriendUsernameField;

    @FXML
    private ListView<User> incomingRequestList;

    @FXML
    protected ListView<User> outgoingRequestList;

    @FXML
    private Button sendRequestButton;

    @FXML
    void onRequestSent(ActionEvent event) {
        System.out.println("Request sent");
        final String username = addFriendUsernameField.textProperty().getValue();

        Platform.runLater(() -> {
            try {
                client.getRelationships().createRelationship(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void initialize() {

        outgoingRequestList.setItems(outgoingList);
        incomingRequestList.setItems(incomingList);

        outgoingRequestList.setCellFactory((r) -> new OutgoingRequestListCell());
        incomingRequestList.setCellFactory((r) -> new IncomingRequestListCell());
        Platform.runLater(() -> {
            try {
                final List<Relationship> relationships = client.getRelationships().fetchRelationships();

                relationships.stream().filter(Relationship::isOutgoing).forEach(r -> {
                            outgoingList.add(r.getUser());
                });

                relationships.stream().filter(Relationship::isIncoming).forEach(r -> {
                    outgoingList.add(r.getUser());
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static class OutgoingRequestListCell extends ListCell<User> {
        private final HBox row = new HBox();
        private final Label userLabel = new Label();
        private final Button withdrawRequest = new Button();

        public OutgoingRequestListCell() {
//            row.prefWidthProperty().bind(outgoingRequestList.widthProperty().subtract(20)); // 1
//            setMaxWidth(Control.USE_PREF_SIZE); //2

            withdrawRequest.setText("Withdraw");

            row.getChildren().add(
                userLabel
            );

            row.getChildren().add(
                    withdrawRequest
            );
        }
        @Override
        protected void updateItem(User item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                userLabel.setText(item.getUsername());
                setGraphic(row);
                setText(null);

                withdrawRequest.setOnAction(a -> {
                    final Optional<Relationship> rel = item.getRelationship();

                    rel.ifPresent(relationship -> {
                        try {
                            relationship.remove();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                });
            }
        }

    }

    private static class IncomingRequestListCell extends ListCell<User> {
        private final HBox row = new HBox();
        private final Label userLabel = new Label();
        private final Button acceptRequest = new Button();
        private final Button denyRequest = new Button();

        public IncomingRequestListCell() {
//            row.prefWidthProperty().bind(outgoingRequestList.widthProperty().subtract(20)); // 1
//            setMaxWidth(Control.USE_PREF_SIZE); //2

            acceptRequest.setText("Accept");
            denyRequest.setText("Withdraw");

            row.getChildren().addAll(
                    userLabel,
                    acceptRequest,
                    denyRequest
            );
        }
        @Override
        protected void updateItem(User item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                userLabel.setText(item.getUsername());
                setGraphic(row);
                setText(null);

                acceptRequest.setOnAction(a -> {
                    final Optional<Relationship> rel = item.getRelationship();

                    rel.ifPresent(relationship -> {
                        try {
                            relationship.getUser().addRelationship();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                });

                denyRequest.setOnAction(a -> {
                    final Optional<Relationship> rel = item.getRelationship();

                    rel.ifPresent(relationship -> {
                        try {
                            relationship.remove();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                });
            }
        }

    }
}



