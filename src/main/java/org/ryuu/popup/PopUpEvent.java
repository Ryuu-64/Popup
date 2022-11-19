package org.ryuu.popup;

import org.ryuu.functional.IFunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PopUpEvent {
    private final List<PopUpEventArgs> popUpEventArgsList = new ArrayList<>();
    private final List<PopUpEventArgs> executePopUpEventArgsList = new ArrayList<>();
    private int nextId = 0;

    public void invoke() {
        if (popUpEventArgsList.size() <= 0) {
            return;
        }

        int priority = popUpEventArgsList.get(0).getPriority();

        if (executePopUpEventArgsList.size() > 0) {
            for (PopUpEventArgs executePopUpEventArgs : executePopUpEventArgsList) {
                if (priority > executePopUpEventArgs.getPriority()) {
                    return;
                }
            }
        }

        for (int i = 0; i < popUpEventArgsList.size(); i++) {
            PopUpEventArgs popUpEventArgs = this.popUpEventArgsList.get(i);
            if (popUpEventArgs.getPriority() != priority) {
                continue;
            }

            i--;
            this.executePopUpEventArgsList.add(popUpEventArgs);
            this.popUpEventArgsList.remove(popUpEventArgs);
            IFunc<PopUp> showPopUp = popUpEventArgs.getShowPopUp();
            if (showPopUp == null) {
                System.out.println(PopUpEvent.class + ", showPopUp is null, " + popUpEventArgs);
                return;
            }

            PopUp popUp = showPopUp.invoke();
            System.out.println(PopUpEvent.class + "popup, " + popUpEventArgs);
            if (popUp == null) {
                System.out.println(PopUpEvent.class + ", popup is null, " + popUpEventArgs);
                return;
            }

            popUp.getOnDispose().add(() -> {
                this.executePopUpEventArgsList.remove(popUpEventArgs);
                System.out.println(PopUpEvent.class + ", popup dispose, " + popUpEventArgs);
                invoke();
            });
        }
    }

    public void add(int priority, IFunc<PopUp> showPopUp) {
        add(nextId, priority, showPopUp);
    }

    private void add(int id, int priority, IFunc<PopUp> showPopUp) {
        if (showPopUp == null) {
            System.out.println(PopUpEvent.class + ", add popup failed, showPopUp can't be null");
            return;
        }
        if (id < 0) {
            System.out.println(PopUpEvent.class + ", add popup failed, id must be non-negative value");
            return;
        }

        PopUpEventArgs popUpEventArgs = new PopUpEventArgs(id, priority, showPopUp);
        for (PopUpEventArgs popUpEventArgsList : this.popUpEventArgsList) {
            if (popUpEventArgsList.getId() == id) {
                System.out.println(PopUpEvent.class + ", add popup failed, repeat id, " + popUpEventArgs);
                return;
            }
        }

        System.out.println(PopUpEvent.class + ", add popup, " + popUpEventArgs);
        this.popUpEventArgsList.add(popUpEventArgs);
        PopUpEventArgs[] popUpEventArgsArray = this.popUpEventArgsList.toArray(new PopUpEventArgs[0]);
        Arrays.sort(popUpEventArgsArray);
        this.popUpEventArgsList.clear();
        this.popUpEventArgsList.addAll(Arrays.asList(popUpEventArgsArray));
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    public List<PopUpEventArgs> getEventArgsList() {
        return new ArrayList<>(popUpEventArgsList);
    }
}