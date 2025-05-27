package org.vut_ija_project.ija.Controller.command;

import org.vut_ija_project.ija.Model.common.Position;
import org.vut_ija_project.ija.Model.game.Game;

public class RotateCommand {
    private final Game game;
    private final Position position;

    public RotateCommand(Game game, Position position) {
        this.game = game;
        this.position = position;
    }

    public void execute() {
        game.rotate(position);
    }

    public void undo() {
        game.rotateBack(position);
    }
}
