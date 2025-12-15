import java.util.*;
import java.text.SimpleDateFormat;

public abstract class BaseFileHandler<T> {
    protected String readFilename;
    protected String writeFilename;
    protected SimpleDateFormat dateFormat;

    public BaseFileHandler(String readFilename, String writeFilename) {
        this.readFilename = readFilename;
        this.writeFilename = writeFilename;
        this.dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    }

    public abstract List<T> readFromFile();
    public abstract void writeToFile(List<T> items);
    protected abstract T parseLine(String line);
}