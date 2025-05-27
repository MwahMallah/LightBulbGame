package org.vut_ija_project.ija.Controller;

import org.vut_ija_project.ija.Common.Events.Event;
import org.vut_ija_project.ija.Common.Events.NewGameEvent;
import org.vut_ija_project.ija.Common.Subscriber;
import org.vut_ija_project.ija.Controller.command.CommandManager;
import org.vut_ija_project.ija.Controller.command.RotateCommand;
import org.vut_ija_project.ija.Controller.entity.GameEntity;
import org.vut_ija_project.ija.Model.common.GameNode;
import org.vut_ija_project.ija.Model.common.Position;
import org.vut_ija_project.ija.Model.creator.GameFileCreator;
import org.vut_ija_project.ija.Model.game.Game;

import java.io.File;
import java.util.Arrays;

public class GameController implements Subscriber {
    private static GameController controller;
    private final CommandManager commandManager;

    private Game currentGame;
    private GameNode[][] nodes;
    private GameController() {
        currentGame = Game.getGame();
        currentGame.subscribe(this);
        commandManager = new CommandManager();
        nodes = currentGame.getNodes();
        newGameRows = 10;
        newGameCols = 10;
        newGameBulbs = 3;
        newGameLinks = 15;
    }

    public static GameController getController() {
        if (controller == null) {
            controller = new GameController();
        }
        return controller;
    }

    public GameEntity[][] getGameEntities() {
        int rows = currentGame.rows();
        int cols = currentGame.cols();
        var entities = new GameEntity[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                var pos = new Position(row, col);
                entities[row][col] = GameEntity.fromGameNode(currentGame.node(pos));
            }
        }

        return entities;
    }

    public void rotate(GameEntity entity) {
        var pos = new Position(entity.row(), entity.col());
        var command = new RotateCommand(currentGame, pos);
        commandManager.executeCommand(command);
    }

    public void undo() {
        commandManager.undo();
    }

    public void redo() {
        commandManager.redo();
    }

    public void saveCurrentGame(File toFile) {
        GameFileCreator.saveGame(currentGame, toFile);
    }

    public void openStoredGame(File fromFile) {
        var newGame = GameFileCreator.createGameFromSource(fromFile);
        currentGame = newGame;
        nodes = currentGame.getNodes();
        System.out.println("Created new game");
        Game.createGameFromTemplate(newGame);
    }

    private int newGameRows;
    private int newGameCols;
    private int newGameBulbs;
    private int newGameLinks;
    public void createNewRandomGame() {
        createNewRandomGame(newGameRows, newGameCols, newGameBulbs, newGameLinks);
    }

    public void createNewRandomGame(int rows, int cols, int bulbs, int links) {
        newGameRows = rows;
        newGameCols = cols;
        newGameLinks = links;
        newGameBulbs = bulbs;
        var newGame = Game.create(rows, cols);
        Game.createGameFromTemplate(newGame);
        newGame.fillBoardRandomly(bulbs, links);
    }

    @Override
    public boolean supports(Event.EventType type) {
        return type == Event.EventType.NEW_GAME;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof NewGameEvent) {
            var newGame = ((NewGameEvent) event).getNewGame();
            currentGame = newGame;
            nodes = currentGame.getNodes();
            newGame.subscribe(this);
        }
    }
}
