package org.ryuu.popup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.ryuu.functional.IFunc;

@AllArgsConstructor
@ToString
public class PopUpEventArgs implements Comparable<PopUpEventArgs> {
    @Getter
    private final int id;
    @Getter
    private final int priority;
    @Getter
    private final IFunc<PopUp> showPopUp;

    @Override
    public int compareTo(PopUpEventArgs popUpEventArgs) {
        return Integer.compare(priority, popUpEventArgs.priority);
    }
}