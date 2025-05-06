package org.vut_ija_project.ija.Common.Events;

public abstract class Event {
    public abstract EventType getType();
    public enum EventType {
        NODE_POWERED,
        NODE_UNPOWERED,
        NEW_GAME,
        GAME_FINISHED
    }
}
