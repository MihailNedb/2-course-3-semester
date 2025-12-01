import java.util.*;

public abstract class Storage<T> {
    protected List<T> items;

    public Storage() {
        this.items = new ArrayList<>();
    }

    public abstract void add(T item);
    public abstract void remove(T item);
    public abstract void displayAll();
    public List<T> getAll() {
        return items;
    }
}