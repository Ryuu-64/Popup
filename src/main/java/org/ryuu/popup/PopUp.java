package org.ryuu.popup;

import org.ryuu.functional.Action;

@FunctionalInterface
public interface PopUp {
    Action getOnDispose();
}