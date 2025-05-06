package org.vut_ija_project.ija.ViewModel;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import org.vut_ija_project.ija.Common.Events.Event;
import org.vut_ija_project.ija.Common.Events.NewGameEvent;
import org.vut_ija_project.ija.Common.Subscriber;
import org.vut_ija_project.ija.Controller.GameController;
import org.vut_ija_project.ija.Controller.entity.GameEntity;
import org.vut_ija_project.ija.Model.game.Game;
import org.vut_ija_project.ija.ViewModel.GameEntityVM.GameEntityVM;

public class GameBoardVM implements Subscriber {
    @FXML
    private Canvas gameCanvas;

    @FXML
    private StackPane gamePane;

    private GraphicsContext gc;

    private GameController gameController;
    private GameEntityVM[][] entities;

    private int cols;
    private int rows;
    private final int tileSize = 50;
    private final String backgroundColor = "#524f4e";

    public void initialize() {
        Game.getGame().subscribe(this);
        gameController = GameController.getController();
        showGame(null);
    }

    public void showGame(Game game) {
        if (game == null) {
            cols = Game.getGame().cols();
            rows = Game.getGame().rows();
        } else {
            cols = game.cols();
            rows = game.rows();
        }
        entities = new GameEntityVM[rows][cols];

        //set canvas and pane size
        setViewSize();

        gc = gameCanvas.getGraphicsContext2D();
        setGameEntities();
        drawInitialState();

        setEventListeners();
    }

    private void setEventListeners() {
        gameCanvas.setOnMouseMoved(this::lightSquare);
        gameCanvas.setOnMouseExited(event -> drawInitialState());
        gameCanvas.setOnMouseClicked(this::rotateEntity);
    }

    private void drawInitialState() {
        fillBackground(backgroundColor);
        drawGameEntities();
        fillGameGrid();
    }

    private int hoveredCol = -1;
    private int hoveredRow = -1;
    private void lightSquare(MouseEvent event) {
        var mouseRow = (int) (event.getY() / tileSize);
        var mouseCol = (int) (event.getX() / tileSize);

        if (hoveredCol == mouseCol && mouseRow == hoveredRow)
            return;

        drawInitialState();
        hoveredCol = mouseCol;
        hoveredRow = mouseRow;
        
        highlightSquare(hoveredCol, hoveredRow);
    }

    private void highlightSquare(int col, int row) {
        gc.setFill(Paint.valueOf("rgba(255,255,0,0.3)"));
        gc.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
    }

    private void fillBackground(String hexColor) {
        gc.setFill(Paint.valueOf(hexColor));
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        gc.fill();
    }

    //create game grid
    private void fillGameGrid() {
        //create vertical lines
        gc.setStroke(Paint.valueOf("#e0dede"));
        gc.setLineWidth(2);
        for (var col = 0; col < cols; col++) {
            gc.strokeLine(col * tileSize, 0, col * tileSize, rows * tileSize);
        }

        //create horizontal lines
        for (var row = 0; row < rows; row++) {
            gc.strokeLine(0, row * tileSize, cols * tileSize, row * tileSize);
        }
    }

    private void drawGameEntities() {
        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < cols; j++) {
                entities[i][j].draw();
            }
        }
    }

    private void setGameEntities() {
        var entities = gameController.getGameEntities();

        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < cols; j++) {
                createGameEntity(entities[i][j], i, j);
            }
        }
    }

    private void createGameEntity(GameEntity entity, int row, int col) {
        var vm = GameEntityVM.create(entity, gc, tileSize, backgroundColor);
        entities[row][col] = vm;
    }

    private void setViewSize() {
        gamePane.setMaxWidth(cols * tileSize);
        gamePane.setMaxHeight(rows * tileSize);
        gameCanvas.setWidth(cols * tileSize);
        gameCanvas.setHeight(rows * tileSize);
    }

    private void rotateEntity(MouseEvent event) {
        var mouseRow = (int) (event.getY() / tileSize);
        var mouseCol = (int) (event.getX() / tileSize);

        entities[mouseRow][mouseCol].rotate();
    }

    @Override
    public boolean supports(Event.EventType type) {
        System.out.println(type);
        return type == Event.EventType.NEW_GAME;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof NewGameEvent) {
            var newGame = ((NewGameEvent) event).getNewGame();
            newGame.subscribe(this);
            showGame(newGame);
            System.out.println("Started new game");
        }
    }
}
