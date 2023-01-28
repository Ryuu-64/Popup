package org.ryuu.popup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.ryuu.functional.Action;

@AllArgsConstructor
@ToString
public class PopUpAdapter implements PopUp {
    @Getter
    private final Action dispose = new Action();
}