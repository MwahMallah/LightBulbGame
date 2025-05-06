package org.vut_ija_project.ija.Common;

import org.vut_ija_project.ija.Common.Events.Event;

public interface Publisher {
    void subscribe(Subscriber subscriber);
}
