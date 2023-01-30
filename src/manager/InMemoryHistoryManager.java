package manager;

import model.Task;

import java.util.List;



public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> requestHistory = new CustomLinkedList<>();

    @Override
    public void addToHistory(Task task) {
        if (requestHistory.contains(task)) {
            removeFromHistory(task.hashCode());
        }
        requestHistory.linkLast(task);
    }

    @Override
    public void removeFromHistory(Integer id) {
        Node<Task> node = requestHistory.getNode(id);
        requestHistory.removeNode(node);
    }

    @Override
    public List<Task> getHistory() {
        return requestHistory.getTasks();
    }
}


