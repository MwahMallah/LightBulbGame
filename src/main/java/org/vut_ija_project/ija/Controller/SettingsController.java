package org.vut_ija_project.ija.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.controlsfx.control.action.Action;

public class SettingsController {
    @FXML
    private TextField rowField;
    @FXML
    private TextField colField;
    @FXML
    private TextField bulbCountField;
    @FXML
    private TextField linkCountField;

    @FXML
    private void onStartGameClicked() {
        try {
            var rows = Integer.valueOf(rowField.getText());
            var cols = Integer.valueOf(colField.getText());
            var bulbs = Integer.valueOf(bulbCountField.getText());
            var links = Integer.valueOf(linkCountField.getText());

            GameController.getController().createNewRandomGame(rows, cols, bulbs, links);
        } catch (NumberFormatException e) {
            System.out.println("No input in fields");
        }
    }
}
