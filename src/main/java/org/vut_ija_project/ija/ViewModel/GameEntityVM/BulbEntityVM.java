package org.vut_ija_project.ija.ViewModel.GameEntityVM;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.vut_ija_project.ija.Common.Events.Event;
import org.vut_ija_project.ija.Common.Events.PowerEvent.NodePoweredEvent;
import org.vut_ija_project.ija.Common.Events.PowerEvent.NodeUnpoweredEvent;
import org.vut_ija_project.ija.Common.Events.PowerEvent.PowerEvent;
import org.vut_ija_project.ija.Controller.entity.GameEntity;
import org.vut_ija_project.ija.Model.game.Game;

public class BulbEntityVM extends GameEntityVM {
    private Image image;
    public BulbEntityVM(GameEntity entity, GraphicsContext gc, int tileSize, String bgColor) {
        super(entity, gc, tileSize, bgColor);
        color = Color.GREY;

        this.image = new Image(getClass().getResourceAsStream("/images/bulb_not_powered.png"));
        Game.getGame().subscribe(this);
    }

    @Override
    public void draw() {
        clear();
        double entitySize = 50;
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

    @Override
    public boolean supports(Event.EventType type) {
        return type == Event.EventType.NODE_POWERED || type == Event.EventType.NODE_UNPOWERED;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof NodePoweredEvent) {
            onNodePowered((NodePoweredEvent) event);
        }

        if (event instanceof NodeUnpoweredEvent) {
            onNodeUnpowered((NodeUnpoweredEvent) event);
        }
    }

    private void onNodeUnpowered(NodeUnpoweredEvent event) {
        if (isEventHere(event)) {
            this.image = new Image(getClass().getResourceAsStream("/images/bulb_not_powered.png"));
            this.color = Color.GREY;
            draw();
        }
    }

    private void onNodePowered(PowerEvent event) {
        if (isEventHere(event)) {
            this.image = new Image(getClass().getResourceAsStream("/images/bulb_powered.png"));
            this.color = Color.YELLOW;
            draw();
        }
    }

    private boolean isEventHere(PowerEvent event) {
        return event.getCol() == entity.col() && event.getRow() == entity.row();
    }
}
