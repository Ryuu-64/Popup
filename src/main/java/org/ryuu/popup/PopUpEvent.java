package org.ryuu.popup;

import org.ryuu.functional.IAction;

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
            for (PopUpEventArgs popUpEventArgs : executePopUpEventArgsList) {
                if (priority > popUpEventArgs.getPriority()) {
                    return;
                }
            }
        }

        for (int i = 0; i < popUpEventArgsList.size(); i++) {
            PopUpEventArgs popUpEventArgs = popUpEventArgsList.get(i);
            if (popUpEventArgs.getPriority() != priority) {
                continue;
            }

            executePopUpEventArgsList.add(popUpEventArgs);
            popUpEventArgsList.remove(popUpEventArgs);
            i--;

            popUpEventArgs.getPopUp().getOnDispose().add(() -> {
                executePopUpEventArgsList.remove(popUpEventArgs);
                System.out.println("[" + this + "] popup dispose, " + popUpEventArgs);
                invoke();
            });
            popUpEventArgs.getShowPopUp().invoke();
            System.out.println("[" + this + "] popup , " + popUpEventArgs);
        }
    }

    public void add(int priority, PopUp popUp, IAction showPopUp) {
        add(nextId, priority, popUp, showPopUp);
    }

    private void add(int id, int priority, PopUp popUp, IAction showPopUp) {
        if (popUp == null) {
            System.out.println("[" + this + "] add popup failed, popUp can't be null");
            return;
        }
        if (showPopUp == null) {
            System.out.println("[" + this + "] add popup failed, showPopUp can't be null");
            return;
        }
        if (id < 0) {
            System.out.println("[" + this + "] add popup failed, id must be non-negative value");
            return;
        }

        PopUpEventArgs popUpEventArgs = new PopUpEventArgs(id, priority, popUp, showPopUp);
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