package org.ryuu.popup;

import lombok.Getter;
import lombok.ToString;
import org.ryuu.functional.IAction1Arg;

@ToString
public class PopUpEvent implements Comparable<PopUpEvent> {
    @Getter
    private final int priority;
    @Getter
    private final PopUp popUp;
    @Getter
    private final IAction1Arg<PopUp> showPopUp;

    public PopUpEvent(int priority, IAction1Arg<PopUp> showPopUp) {
        this.priority = priority;
        this.popUp = new PopUpAdapter();
        this.showPopUp = showPopUp;
    }

    @Override
    public int compareTo(PopUpEvent popUpEvent) {
        return Integer.compare(priority, popUpEvent.priority);
    }
}