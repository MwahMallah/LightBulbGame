package org.vut_ija_project.ija.Common.Events;

import org.vut_ija_project.ija.Model.game.Game;

public class NewGameEvent extends Event {
    public Game newGame;
    public NewGameEvent(Game newGame) {
        super();
        this.newGame = newGame;
    }

    @Override
    public EventType getType() {
        return EventType.NEW_GAME;
    }

    public Game getNewGame() {return this.newGame;}
}
