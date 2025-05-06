package org.vut_ija_project.ija.Common.Events;

import static org.vut_ija_project.ija.Common.Events.Event.EventType.GAME_FINISHED;

public class FinishedGameEvent extends Event {
    @Override
    public EventType getType() {
        return GAME_FINISHED;
    }
}
