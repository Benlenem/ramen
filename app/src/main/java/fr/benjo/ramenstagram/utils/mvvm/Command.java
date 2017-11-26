package fr.benjo.ramenstagram.utils.mvvm;

/**
 * Created by Benjamin Orsini on 28/07/16.
 */
public abstract class Command {
    public static Command EMPTY_COMMAND = new Command() {
        @Override
        public void execute() {
        }
    };
    public static Command1 EMPTY_COMMAND1 = new Command1() {
        @Override
        public void execute(Object param) {
        }
    };

    public abstract void execute();

    public boolean canExecute() {
        return true;
    }
}
