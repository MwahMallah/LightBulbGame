package org.vut_ija_project.ija.Common.Events;

import org.vut_ija_project.ija.Model.common.GameNode;
import org.vut_ija_project.ija.Model.common.Position;

import static org.vut_ija_project.ija.Common.Events.Event.EventType.NODE_TURNED_BACK;

public class NodeTurnedBackEvent extends Event {
    private final GameNode node;

    public NodeTurnedBackEvent(GameNode node) {
        this.node = node;
    }

    public Position getPosition() {
        return node .getPosition();
    }

    @Override
    public EventType getType() {
        return NODE_TURNED_BACK;
    }
}
