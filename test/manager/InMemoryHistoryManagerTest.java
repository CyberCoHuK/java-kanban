package manager;

import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager = Manager.getDefaultHistory();

    @Test
    void addToHistory() {
        Task task = new Task(1, "Task", "Task description");
        historyManager.addToHistory(task);
        assertNotNull(historyManager.getHistory(), "История пустая");
        assertEquals(1, historyManager.getHistory().size(), "История не верного размера");
        historyManager.addToHistory(task);
        assertEquals(1, historyManager.getHistory().size(), "История не верного размера");
    }

    @Test
    void removeFromHistory() {
        Task task1 = new Task(1, "Task1", "Task description");
        Task task2 = new Task(2, "Task2", "Task description");
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);
        assertEquals(2, historyManager.getHistory().size(), "История пуста");
        historyManager.removeFromHistory(task1.getId());
        assertNotNull(historyManager.getHistory(), "История пуста");
        assertEquals(1, historyManager.getHistory().size(), "История пуста");
        historyManager.removeFromHistory(task2.getId());
    }

    @Test
    void getHistory() {
        Task task1 = new Task(1, "Task1", "Task description");
        historyManager.addToHistory(task1);
        assertNotNull(historyManager.getHistory(), "История пуста");
    }
}