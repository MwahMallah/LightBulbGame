package org.vut_ija_project.ija.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

public class MenuController {
    @FXML
    public MenuItem openMenuItem;
    @FXML
    private MenuItem saveMenuItem;
    GameController gameController;

    public void initialize() {
        saveMenuItem.setOnAction(this::saveGame);
        openMenuItem.setOnAction(this::openGame);
        gameController = GameController.getController();
    }

    public void saveGame(ActionEvent event) {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file where to store current game");

        var chosenFile = fileChooser.showSaveDialog(saveMenuItem.getParentPopup().getScene().getWindow());
        if (chosenFile != null) {
            gameController.saveCurrentGame(chosenFile);
        }
    }

    public void openGame(ActionEvent event) {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file from where to open new game");

        var chosenFile = fileChooser.showOpenDialog(saveMenuItem.getParentPopup().getScene().getWindow());
        if (chosenFile != null) {
            gameController.openStoredGame(chosenFile);
        }
    }
}
