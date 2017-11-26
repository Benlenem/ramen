package fr.benjo.ramenstagram.utils.mvvm;

/**
 * Created by Benjamin Orsini on 28/07/16.
 */
public abstract class Command1<T> {
    public abstract void execute(T param);

    public boolean canExecute() {
        return true;
    }
}
