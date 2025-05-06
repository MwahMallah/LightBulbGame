package org.vut_ija_project.ija.ViewModel.GameEntityVM;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.vut_ija_project.ija.Controller.entity.GameEntity;

public class EmptyEntityVM extends GameEntityVM {
    public EmptyEntityVM(GameEntity entity, GraphicsContext gc, int tileSize, String bgColor) {
        super(entity, gc, tileSize, bgColor);
        color = Color.BLUE;
    }

    @Override
    public void draw()
        {}
}
