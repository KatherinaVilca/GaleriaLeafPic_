package org.horaapps.leafpic.imageEditor;

import java.io.Serializable;
import java.util.Stack;

public class History implements Serializable {

    public Stack<CommandEditor> historial ;

    public History(){
        historial= new Stack<CommandEditor>();
    }

    public void push(CommandEditor c) {
        historial.push(c);
    }

    public CommandEditor pop() {
        return historial.pop();
    }

    public boolean isEmpty() {
        return historial.isEmpty();
    }


}
