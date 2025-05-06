package org.vut_ija_project.ija.ViewModel.GameEntityVM;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.vut_ija_project.ija.Controller.entity.GameEntity;

public class PowerEntityVM extends GameEntityVM {
    private final Image image;
    public PowerEntityVM(GameEntity entity, GraphicsContext gc, int tileSize, String bgColor) {
        super(entity, gc, tileSize, bgColor);
        color = Color.YELLOW;

        this.image = new Image(getClass().getResourceAsStream("/images/generator.png"));
    }

    @Override
    public void draw() {
        clear();
        double entitySize = 55;
        double offset = (tileSize - entitySize) / 2;

        double x = entity.col() * tileSize + offset;
        double y = entity.row() * tileSize + offset;

        gc.save(); // Save current state

        // translate coordinate origin to center of entity
        gc.translate(x + entitySize / 2, y + entitySize / 2);
        gc.rotate(rotationAngle);

        drawSides();

        // draw image centered at origin
        gc.drawImage(image, -entitySize / 2, -entitySize / 2, entitySize, entitySize);

        gc.setStroke(Paint.valueOf("#e0dede"));
        gc.strokeRect((double) -tileSize / 2, (double) -tileSize / 2, tileSize, tileSize);

        gc.restore(); // Restore initial state
    }
}
