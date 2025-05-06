package org.vut_ija_project.ija.Applicaiton;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.vut_ija_project.ija.Model.common.Position;
import org.vut_ija_project.ija.Model.common.Side;
import org.vut_ija_project.ija.Model.creator.GameFileCreator;
import org.vut_ija_project.ija.Model.game.Game;

import java.io.File;
import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        Game.getGame().fillBoardRandomly(3, 15);
        //load fxml
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/mainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle("Light bulb puzzle!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}