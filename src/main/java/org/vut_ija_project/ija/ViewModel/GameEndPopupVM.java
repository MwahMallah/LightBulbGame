package org.vut_ija_project.ija.ViewModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.vut_ija_project.ija.Common.Events.Event;
import org.vut_ija_project.ija.Common.Events.FinishedGameEvent;
import org.vut_ija_project.ija.Common.Events.NewGameEvent;
import org.vut_ija_project.ija.Common.Subscriber;
import org.vut_ija_project.ija.Controller.GameController;
import org.vut_ija_project.ija.Model.game.Game;

public class GameEndPopupVM implements Subscriber {
    public void initialize() {
        Game.getGame().subscribe(this);
    }

    @FXML
    private StackPane popupRoot;

    public void onNewGameClicked(ActionEvent event) {
        GameController.getController().createNewRandomGame();
        popupRoot.setVisible(false);
    }

    @Override
    public boolean supports(Event.EventType type) {
        return type == Event.EventType.GAME_FINISHED || type == Event.EventType.NEW_GAME;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof FinishedGameEvent) {
            popupRoot.setVisible(true);
        }

        if (event instanceof NewGameEvent) {
            ((NewGameEvent) event).getNewGame().subscribe(this);
        }
    }
}
