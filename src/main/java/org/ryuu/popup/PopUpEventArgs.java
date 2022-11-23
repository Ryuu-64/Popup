package org.ryuu.popup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.ryuu.functional.IAction;

@AllArgsConstructor
@ToString
public class PopUpEventArgs implements Comparable<PopUpEventArgs> {
    @Getter
    private final int id;
    @Getter
    private final int priority;
    @Getter
    private final PopUp popUp;
    @Getter
    private final IAction showPopUp;

    @Override
    public int compareTo(PopUpEventArgs popUpEventArgs) {
        return Integer.compare(priority, popUpEventArgs.priority);
    }
}
