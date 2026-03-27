package org.uwgb.compsci330.frontend.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class FXMLUtils {
    public static <T> LoadedView<T> load(String path, T controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(FXMLUtils.class.getResource(path));
        loader.setControllerFactory(c -> controller);
        Parent root = loader.load();
        root.getStylesheets().add(
                FXMLUtils.class.getResource("/css/style.css").toExternalForm()
        );
        return new LoadedView<>(root, loader.getController());
    }

    public record LoadedView<T>(Parent root, T controller) {}
}
