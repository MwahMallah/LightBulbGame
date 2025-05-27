package org.vut_ija_project.ija.Controller.command;

import org.vut_ija_project.ija.Common.Events.Event;
import org.vut_ija_project.ija.Common.Events.NewGameEvent;
import org.vut_ija_project.ija.Common.Subscriber;
import org.vut_ija_project.ija.Model.game.Game;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

public class CommandManager implements Subscriber {
    private final Stack<RotateCommand> undoStack;
    private final Stack<RotateCommand> redoStack;
    public CommandManager() {
        Game.getGame().subscribe(this);
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void executeCommand(RotateCommand command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            var command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            var command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }

    @Override
    public boolean supports(Event.EventType type) {
        return type == Event.EventType.NEW_GAME;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof NewGameEvent) {
            undoStack.clear();
            redoStack.clear();
        }
    }
}
