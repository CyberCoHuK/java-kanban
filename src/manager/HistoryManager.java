package manager;

import model.Task;

import java.util.ArrayList;

public interface HistoryManager {
    int HISTORY_SIZE = 10;

    void addToHistory(Task task);

    ArrayList<Task> getHistory();
}
