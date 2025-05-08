package org.vut_ija_project.ija.Common.Events;

public class GameSetEvent extends Event {
    @Override
    public EventType getType() {
        return EventType.GAME_SET;
    }
}
