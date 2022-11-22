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
            PopUpEventArgs popUpEventArgsInList = popUpEventArgsList.get(i);
            if (popUpEventArgsInList.getPriority() != priority) {
                continue;
            }

            i--;
            executePopUpEventArgsList.add(popUpEventArgsInList);
            popUpEventArgsList.remove(popUpEventArgsInList);
            IFunc<PopUp> showPopUp = popUpEventArgsInList.getShowPopUp();
            if (showPopUp == null) {
                System.out.println("[" + this + "] showPopUp is null, " + popUpEventArgsInList);
                return;
            }

            PopUp popUp = showPopUp.invoke();
            System.out.println("[" + this + "] popup, " + popUpEventArgsInList);
            if (popUp == null) {
                System.out.println("[" + this + "] popup is null, " + popUpEventArgsInList);
                return;
            }

            popUp.getOnDispose().add(() -> {
                executePopUpEventArgsList.remove(popUpEventArgsInList);
                System.out.println("[" + this + "] popup dispose, " + popUpEventArgsInList);
                invoke();
            });
        }
    }

    public void add(int priority, IFunc<PopUp> showPopUp) {
        add(nextId, priority, showPopUp);
    }

    private void add(int id, int priority, IFunc<PopUp> showPopUp) {
        if (showPopUp == null) {
            System.out.println("[" + this + "] add popup failed, showPopUp can't be null");
            return;
        }
        if (id < 0) {
            System.out.println("[" + this + "] add popup failed, id must be non-negative value");
            return;
        }

        PopUpEventArgs popUpEventArgs = new PopUpEventArgs(id, priority, showPopUp);
        for (PopUpEventArgs popUpEventArgsInList : popUpEventArgsList) {
            if (popUpEventArgsInList.getId() == id) {
                System.out.println("[" + this + "] add popup failed, repeat id, " + popUpEventArgsInList);
                return;
            }
        }

        System.out.println("[" + this + "] add popup, " + popUpEventArgs);
        popUpEventArgsList.add(popUpEventArgs);
        PopUpEventArgs[] popUpEventArgsArray = popUpEventArgsList.toArray(new PopUpEventArgs[0]);
        Arrays.sort(popUpEventArgsArray);
        popUpEventArgsList.clear();
        popUpEventArgsList.addAll(Arrays.asList(popUpEventArgsArray));
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    public List<PopUpEventArgs> getEventArgsList() {
        return new ArrayList<>(popUpEventArgsList);
    }
}