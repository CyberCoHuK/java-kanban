package manager;

public class Manager {
    public static TaskManager getDefault() {
        return new FileBackedTasksManager("file.csv");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
