package org.uwgb.compsci330.termfinal;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FXApplication extends Application {
    private VBox createLoginScreen() {
        Label loginLabel = new Label("Please Login");

        VBox loginActions = new VBox();
        loginActions.setAlignment(Pos.CENTER);
        loginActions.setSpacing(20);
        loginActions.setPrefWidth(100);

        Button signUpBtn = new Button("Sign Up Instead");
        Button signInBtn = new Button("Sign In");

        signUpBtn.setMinWidth(loginActions.getPrefWidth());
        signInBtn.setMinWidth(loginActions.getPrefWidth());

        loginActions.getChildren().addAll(signUpBtn, signInBtn);

        TextField usernameField = new TextField();
        TextField passwordField = new PasswordField();

        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");

        VBox loginForm = new VBox(usernameField, passwordField, loginActions);
        loginForm.setSpacing(20f);

        VBox screen = new VBox(loginLabel, loginForm);
        screen.setAlignment(Pos.CENTER);

        screen.setPadding(new Insets(20, 20, 20,20));

        return screen;
    }
    @Override
    public void start(Stage stage) {
        VBox loginScreen = this.createLoginScreen();
        Scene scene = new Scene(loginScreen, 640, 480);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
