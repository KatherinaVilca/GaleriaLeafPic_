package org.horaapps.leafpic.imageEditor;

import java.io.Serializable;
import java.util.Observable;
import java.util.Stack;

public class HistorialObserver extends Observable implements Serializable {

    private Stack<CommandEditor> historial = new Stack<CommandEditor>();
    private static HistorialObserver hObs= new HistorialObserver();

    public static HistorialObserver getInstance(){
        return hObs;
    }

    public void push(CommandEditor c) {

        historial.push(c);
        setChanged();
        notifyObservers(historial);
    }

    public CommandEditor pop() {
        CommandEditor cm= historial.pop();
        setChanged();
        notifyObservers(historial);
        return cm;
    }

}
