package org.vut_ija_project.ija.ViewModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.vut_ija_project.ija.Common.Events.Event;
import org.vut_ija_project.ija.Common.Subscriber;
import org.vut_ija_project.ija.Controller.GameController;
import org.vut_ija_project.ija.Model.game.Game;

public class GameControlVM implements Subscriber {
    @FXML
    private Button forwardBtn;
    @FXML
    private Button backBtn;

    public void initialize() {
        Game.getGame().subscribe(this);
        backBtn.setOnAction(this::onBackBtnPressed);
        forwardBtn.setOnAction(this::onForwardBtnPressed);
    }

    private void onBackBtnPressed(ActionEvent event) {
        GameController.getController().undo();
    }

    private void onForwardBtnPressed(ActionEvent event) {
        GameController.getController().redo();
    }

    @Override
    public boolean supports(Event.EventType type) {
        return type == Event.EventType.GAME_FINISHED || type == Event.EventType.NEW_GAME;
    }

    @Override
    public void onEvent(Event event) {

    }
}
