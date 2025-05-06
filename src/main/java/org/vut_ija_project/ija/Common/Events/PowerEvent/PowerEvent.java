package org.vut_ija_project.ija.Common.Events.PowerEvent;

import org.vut_ija_project.ija.Common.Events.Event;
import org.vut_ija_project.ija.Model.common.GameNode;

public abstract class PowerEvent extends Event {
    protected final GameNode node;

    public PowerEvent(GameNode node) {
        this.node = node;
    }

    public int getRow() {
        return node.getPosition().getRow();
    }

    public int getCol() {
        return node.getPosition().getCol();
    }
}
