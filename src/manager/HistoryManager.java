package manager;

import model.Task;

import java.util.List;

public interface HistoryManager {

    void addToHistory(Task task);

    void removeFromHistory(Integer id);

    List<Task> getHistory();
}
