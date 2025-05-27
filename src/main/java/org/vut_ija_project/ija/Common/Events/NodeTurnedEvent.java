package org.vut_ija_project.ija.Common.Events;

import org.vut_ija_project.ija.Model.common.GameNode;
import org.vut_ija_project.ija.Model.common.Position;

import static org.vut_ija_project.ija.Common.Events.Event.EventType.NODE_TURNED;
import static org.vut_ija_project.ija.Common.Events.Event.EventType.NODE_TURNED_BACK;

public class NodeTurnedEvent extends Event {
    private final GameNode node;

    public NodeTurnedEvent(GameNode node) {
        this.node = node;
    }

    public Position getPosition() {
        return node.getPosition();
    }
    @Override
    public Event.EventType getType() {
        return NODE_TURNED;
    }
}
