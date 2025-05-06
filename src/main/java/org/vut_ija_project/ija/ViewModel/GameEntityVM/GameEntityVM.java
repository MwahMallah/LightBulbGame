package org.vut_ija_project.ija.ViewModel.GameEntityVM;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.vut_ija_project.ija.Common.Events.Event;
import org.vut_ija_project.ija.Common.Subscriber;
import org.vut_ija_project.ija.Controller.GameController;
import org.vut_ija_project.ija.Controller.entity.GameEntity;

public abstract class GameEntityVM implements Subscriber {
    private final GameController gameController;
    protected GameEntity entity;
    protected GraphicsContext gc;
    protected int tileSize;
    private final String bgColor;
    protected Color color;
    protected int rotationAngle = 0;

    public GameEntityVM(GameEntity entity, GraphicsContext gc, int tileSize, String bgColor) {
        this.gameController = GameController.getController();
        this.entity = entity;
        this.gc = gc;
        this.tileSize = tileSize;
        this.bgColor = bgColor;
    }

    public static GameEntityVM create(GameEntity entity, GraphicsContext gc, int tileSize, String backgroundColor) {
        switch (entity.type()) {
            case Empty -> {
                return new EmptyEntityVM(entity, gc, tileSize, backgroundColor);
            }
            case Link -> {
                return new LinkEntityVM(entity, gc, tileSize, backgroundColor);
            }
            case Power -> {
                return new PowerEntityVM(entity, gc, tileSize, backgroundColor);
            }
            case Bulb -> {
                return new BulbEntityVM(entity, gc, tileSize, backgroundColor);
            }
        }

        throw new Error();
    }

    public void draw() {
        //clear
        clear();

        double entitySize = 20;
        double offset = (tileSize - entitySize) / 2;

        double x = entity.col() * tileSize + offset;
        double y = entity.row() * tileSize + offset;

        gc.save(); // Save current state

        // translate coordinate origin to center of entity
        gc.translate(x + entitySize / 2, y + entitySize / 2);
        gc.rotate(rotationAngle);

        // relative to new coordinate system
        gc.setFill(color);
        gc.fillRect(-entitySize / 2, -entitySize / 2, entitySize, entitySize);

        gc.setStroke(Color.BLACK);
        gc.strokeRect(-entitySize / 2, -entitySize / 2, entitySize, entitySize);

        gc.restore(); // Restore initial state
    }

    public void rotate() {
        rotationAngle = (rotationAngle + 90) % 360;
        draw();
        gameController.rotate(this.entity);
    }

    public void clear() {
        double x = entity.col() * tileSize;
        double y = entity.row() * tileSize;

        gc.setFill(Paint.valueOf(bgColor));
        gc.fillRect(x, y, tileSize, tileSize);
    }

    protected void drawSides() {
        gc.setStroke(color);
        gc.setLineWidth(2);

        double halfTile = (double) tileSize / 2 - 1;

        for (var side : entity.sides()) {
            switch (side) {
                case NORTH -> gc.strokeLine(0, 0, 0, -halfTile);
                case SOUTH -> gc.strokeLine(0, 0, 0, halfTile);
                case EAST  -> gc.strokeLine(0, 0, halfTile, 0);
                case WEST  -> gc.strokeLine(0, 0, -halfTile, 0);
            }
        }
    }

    @Override
    public boolean supports(Event.EventType type) {
        return false;
    }

    @Override
    public void onEvent(Event event) {

    }
}
