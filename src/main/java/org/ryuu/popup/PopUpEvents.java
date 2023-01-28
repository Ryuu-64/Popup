package org.ryuu.popup;

import lombok.Getter;
import org.ryuu.functional.Action1Arg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class PopUpEvents {
    @Getter
    private static final Logger logger = Logger.getLogger(PopUpEvents.class.getName());
    private final List<PopUpEvent> popUpEvents = new ArrayList<>();
    private final List<PopUpEvent> executePopUpEvents = new ArrayList<>();

    public void invoke() {
        if (popUpEvents.isEmpty()) {
            return;
        }

        int nextPriority = popUpEvents.get(0).getPriority();
        if (!executePopUpEvents.isEmpty() && executePopUpEvents.stream().anyMatch(event -> nextPriority > event.getPriority())) {
            return;
        }

        for (int i = 0; i < popUpEvents.size(); i++) {
            PopUpEvent popUpEvent = popUpEvents.get(i);
            if (popUpEvent.getPriority() != nextPriority) {
                continue;
            }
            i--;
            executePopUpEvents.add(popUpEvent);
            popUpEvents.remove(popUpEvent);
            logger.info("[" + this + "] popup , " + popUpEvent);
            popUpEvent.getShowPopUp().invoke(popUpEvent.getPopUp());
        }
    }

    public PopUpEvent add(int priority, Action1Arg<PopUp> showPopUp) {
        if (showPopUp == null) {
            logger.warning("[" + this + "] add popup failed, showPopUp can't be null");
            return null;
        }

        PopUpEvent popUpEvent = new PopUpEvent(priority, showPopUp);

        popUpEvent.getPopUp().getDispose().add(() -> {
            executePopUpEvents.remove(popUpEvent);
            logger.info("[" + this + "] popup dispose, " + popUpEvent);
            invoke();
        });

        logger.info("[" + this + "] add popup, " + popUpEvent);
        popUpEvents.add(popUpEvent);
        Collections.sort(popUpEvents);
        return popUpEvent;
    }

    public boolean remove(PopUpEvent popUpEvent) {
        return popUpEvents.remove(popUpEvent);
    }

    public List<PopUpEvent> getEventList() {
        return new ArrayList<>(popUpEvents);
    }
}