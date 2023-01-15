package manager;

import model.Task;

import java.util.List;

public interface HistoryManager {
    int HISTORY_SIZE = 10;

    void addToHistory(Task task);

    List<Task> getHistory();
}
