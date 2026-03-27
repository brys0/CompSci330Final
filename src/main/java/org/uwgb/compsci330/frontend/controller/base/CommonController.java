package org.uwgb.compsci330.frontend.controller.base;

import javafx.scene.Parent;
import javafx.stage.Stage;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.frontend.util.FXMLUtils;

import java.io.IOException;

public class CommonController {
    final protected Stage stage;
    final protected Client client;
    protected Parent parent;


    public CommonController(Parent parent, Stage stage, Client client) {
        this.stage = stage;
        this.client = client;
        this.parent = parent;
    }

    protected <T> void navigateTo(String path, T controller) throws IOException {
        final FXMLUtils.LoadedView<T> view = FXMLUtils.load(path, controller);

        this.parent = view.root();
        stage.getScene().setRoot(view.root());
    }

    protected <T> void navigateTo(Stage targetStage, String path, T controller) throws IOException {
        FXMLUtils.LoadedView<T> view = FXMLUtils.load(path, controller);
        targetStage.getScene().setRoot(view.root());
    }

    protected <T> void navigateToFullscreen(String path, T controller) throws IOException {
        this.navigateTo(path, controller);

        stage.setResizable(true);
        stage.setFullScreen(true);
    }
}
