package org.ryuu.popup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.ryuu.functional.IAction1Arg;

@AllArgsConstructor
@ToString
public class PopUpEventArgs implements Comparable<PopUpEventArgs> {
    @Getter
    private final int priority;
    @Getter
    private final PopUp popUp;
    @Getter
    private final IAction1Arg<PopUp> showPopUp;

    @Override
    public int compareTo(PopUpEventArgs popUpEventArgs) {
        return Integer.compare(priority, popUpEventArgs.priority);
    }
}
