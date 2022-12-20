package org.ryuu.popup;

import lombok.Getter;
import org.ryuu.functional.IAction1Arg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class PopUpEvent {
    @Getter
    private static final Logger logger = Logger.getLogger(PopUpEvent.class.getName());
    private final List<PopUpEventArgs> popUpEventArgsList = new ArrayList<>();
    private final List<PopUpEventArgs> executePopUpEventArgsList = new ArrayList<>();

    public void invoke() {
        if (popUpEventArgsList.isEmpty()) {
            return;
        }

        int nextPriority = popUpEventArgsList.get(0).getPriority();

        if (!executePopUpEventArgsList.isEmpty() && executePopUpEventArgsList.stream().anyMatch(popUpEventArgs -> nextPriority > popUpEventArgs.getPriority())) {
            return;
        }
        for (int i = 0; i < popUpEventArgsList.size(); i++) {
            PopUpEventArgs popUpEventArgs = popUpEventArgsList.get(i);
            if (popUpEventArgs.getPriority() != nextPriority) {
                continue;
            }
            i--;
            executePopUpEventArgsList.add(popUpEventArgs);
            popUpEventArgsList.remove(popUpEventArgs);
            logger.info("[" + this + "] popup , " + popUpEventArgs);
            popUpEventArgs.getShowPopUp().invoke(popUpEventArgs.getPopUp());
        }
    }

    public PopUpEventArgs add(int priority, PopUp popUp, IAction1Arg<PopUp> showPopUp) {
        if (popUp == null) {
            logger.warning("[" + this + "] add popup failed, popUp can't be null");
            return null;
        }
        if (showPopUp == null) {
            logger.warning("[" + this + "] add popup failed, showPopUp can't be null");
            return null;
        }

        PopUpEventArgs popUpEventArgs = new PopUpEventArgs(priority, popUp, showPopUp);
        popUpEventArgs.getPopUp().getOnDispose().add(() -> {
            executePopUpEventArgsList.remove(popUpEventArgs);
            logger.info("[" + this + "] popup dispose, " + popUpEventArgs);
            invoke();
        });

        logger.info("[" + this + "] add popup, " + popUpEventArgs);
        popUpEventArgsList.add(popUpEventArgs);
        Collections.sort(popUpEventArgsList);
        return popUpEventArgs;
    }

    public boolean remove(PopUpEventArgs popUpEventArgs) {
        return popUpEventArgsList.remove(popUpEventArgs);
    }

    public List<PopUpEventArgs> getEventArgsList() {
        return new ArrayList<>(popUpEventArgsList);
    }
}