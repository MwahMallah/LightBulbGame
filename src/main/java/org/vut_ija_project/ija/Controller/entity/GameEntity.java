package org.vut_ija_project.ija.Controller.entity;

import org.vut_ija_project.ija.Model.common.GameNode;
import org.vut_ija_project.ija.Model.common.Side;

import java.util.Set;

public record GameEntity(int col, int row, GameNode.Type type, Set<Side> sides)
{
    public static GameEntity fromGameNode(GameNode gameNode) {
        var pos = gameNode.getPosition();
        return new GameEntity(pos.getCol(), pos.getRow(), gameNode.getType(), gameNode.getConnectedSides());
    }
}
