package manager;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> requestHistory = new ArrayList<>();

    @Override
    public void addToHistory(Task task) {
        if (task != null) {
            if (requestHistory.size() == HISTORY_SIZE) {
                requestHistory.remove(0);
            }
            requestHistory.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return requestHistory;
    }
}
