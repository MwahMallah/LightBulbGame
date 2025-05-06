package org.vut_ija_project.ija.Common.Events.PowerEvent;

import org.vut_ija_project.ija.Common.Events.Event;
import org.vut_ija_project.ija.Model.common.GameNode;

public class NodeUnpoweredEvent extends PowerEvent {
    public NodeUnpoweredEvent(GameNode node) {
        super(node);
    }

    @Override
    public Event.EventType getType() {
        return Event.EventType.NODE_UNPOWERED;
    }
}
