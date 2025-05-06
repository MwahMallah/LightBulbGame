package org.vut_ija_project.ija.ViewModel.GameEntityVM;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.vut_ija_project.ija.Common.Events.Event;
import org.vut_ija_project.ija.Common.Events.PowerEvent.NodePoweredEvent;
import org.vut_ija_project.ija.Common.Events.PowerEvent.NodeUnpoweredEvent;
import org.vut_ija_project.ija.Common.Events.PowerEvent.PowerEvent;
import org.vut_ija_project.ija.Controller.entity.GameEntity;
import org.vut_ija_project.ija.Model.game.Game;

public class LinkEntityVM extends GameEntityVM {
    public LinkEntityVM(GameEntity entity, GraphicsContext gc, int tileSize, String bgColor) {
        super(entity, gc, tileSize, bgColor);
        color = Color.GREY;
        Game.getGame().subscribe(this);
    }

    @Override
    public void draw() {
        clear();

        double x = entity.col() * tileSize;
        double y = entity.row() * tileSize;

        gc.save(); // Save current state

        // translate coordinate system to center of tile
        gc.translate(x + (double) tileSize / 2, y + (double) tileSize / 2);
        gc.rotate(rotationAngle);

        drawSides();

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
            color = Color.GREY;
            draw();
        }
    }

    private void onNodePowered(PowerEvent event) {
        if (isEventHere(event)) {
            color = Color.YELLOW;
            draw();
        }
    }

    private boolean isEventHere(PowerEvent event) {
        return event.getCol() == entity.col() && event.getRow() == entity.row();
    }
}
