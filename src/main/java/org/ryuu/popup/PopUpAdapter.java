package org.ryuu.popup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.ryuu.functional.Actions;

@AllArgsConstructor
@ToString
public class PopUpAdapter implements PopUp {
    @Getter
    private final Actions dispose = new Actions();
}