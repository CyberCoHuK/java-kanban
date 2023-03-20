package manager;

public class Manager {
    public static TaskManager getDefault() {
        return new HttpTaskManager(8078);
    }
    public static TaskManager getFileBacked() {
        return new FileBackedTasksManager("fileHttp.csv");
    }
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
