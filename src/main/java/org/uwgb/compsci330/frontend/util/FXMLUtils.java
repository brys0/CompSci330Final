package org.uwgb.compsci330.frontend.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;

public class FXMLUtils {
    public static <T> LoadedView<T> load(String path, T controller, boolean shouldLoadStyles) throws IOException {
        FXMLLoader loader = new FXMLLoader(FXMLUtils.class.getResource(path));
        loader.setControllerFactory(c -> controller);
        Parent root = loader.load();
        if (shouldLoadStyles) {
            root.getStylesheets().add(
                    Objects.requireNonNull(FXMLUtils.class.getResource("/css/style.css")).toExternalForm()
            );
        }

        return new LoadedView<>(root, loader.getController());
    }

    public record LoadedView<T>(Parent root, T controller) {}
}
