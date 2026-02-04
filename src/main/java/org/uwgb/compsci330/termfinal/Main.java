package org.uwgb.compsci330.termfinal;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// We need to create a generic main class
// That just calls our JavaFX class
// due to runtime dependency issues.
public class Main {
    public static void main(String[] args) {
        FXApplication.main(args);
    }
}