package org.uwgb.compsci330.frontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.entity.user.User;
import org.uwgb.compsci330.frontend.controller.base.CommonController;
import org.uwgb.compsci330.frontend.util.FXMLUtils;

import java.io.IOException;

public class FriendController extends CommonController {

    private final Stage ownerStage;

    public FriendController(Parent parent, Stage stage, Stage ownerStage, Client client) {
        super(parent, stage, client);
        this.ownerStage = ownerStage;
    }

    public static void open(Stage owner, Client client) throws IOException {
        Stage settingsStage = new Stage();
        settingsStage.setTitle("Settings");
        settingsStage.initStyle(StageStyle.UNDECORATED);
        settingsStage.initOwner(owner);

        FriendController controller = new FriendController(null, settingsStage, owner, client);
        FXMLUtils.LoadedView<FriendController> view = FXMLUtils.load(
                "/xml/pages/friends/friend_popup.fxml",
                controller
        );

        Scene scene = new Scene(view.root());

        settingsStage.setScene(scene);
        settingsStage.show();
    }

    @FXML
    private TextField addFriendUsernameField;

    @FXML
    private static ListView<User> incomingRequestList;

    @FXML
    protected static ListView<User> outgoingRequestList;

    @FXML
    private Button sendRequestButton;

    @FXML
    void onRequestSent(ActionEvent event) {

    }

    @FXML
    public void initialize() {
        client.getRelationships();
    }

    private static class OutgoingRequestListCell extends ListCell<User> {
        private final HBox row = new HBox();
        private final Label userLabel = new Label();
        private final Button withdrawRequest = new Button();

        public OutgoingRequestListCell() {
            row.prefWidthProperty().bind(outgoingRequestList.widthProperty().subtract(20)); // 1
            setMaxWidth(Control.USE_PREF_SIZE); //2

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

            }
        }

    }
}



