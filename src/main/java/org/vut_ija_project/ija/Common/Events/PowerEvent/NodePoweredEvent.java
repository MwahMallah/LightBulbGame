package org.vut_ija_project.ija.Common.Events.PowerEvent;

import org.vut_ija_project.ija.Common.Events.Event;
import org.vut_ija_project.ija.Model.common.GameNode;

public class NodePoweredEvent extends PowerEvent {
    public NodePoweredEvent(GameNode node) {
        super(node);
    }

    @Override
    public EventType getType() {
        return EventType.NODE_POWERED;
    }
}
