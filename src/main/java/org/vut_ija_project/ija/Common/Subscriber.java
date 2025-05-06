package org.vut_ija_project.ija.Common;

import org.vut_ija_project.ija.Common.Events.Event;

public interface Subscriber {
    boolean supports(Event.EventType type);
    void onEvent(Event event);
}
