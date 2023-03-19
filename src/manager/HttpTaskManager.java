package manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exception.HistoryManagerException;
import model.*;
import server.KVTaskClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    public static final String TASKS = "tasks";
    public static final String EPICS = "epics";
    public static final String SUBTASKS = "subtasks";
    public static final String HISTORY = "history";
    private final KVTaskClient kvTaskClient;
    private final Gson gson = new Gson();

    public HttpTaskManager(int port) {
        super("file.csv");
        kvTaskClient = new KVTaskClient(port);
    }

    public void save() {
        if (getTasks().size() > 0) {
            String allTasks = gson.toJson(getTasks());
            kvTaskClient.put(TASKS, allTasks);
        }
        if (getEpics().size() > 0) {
            String allEpics = gson.toJson(getEpics());
            kvTaskClient.put(EPICS, allEpics);
        }
        if (getSubtasks().size() > 0) {
            String allSubTasks = gson.toJson(getSubtasks());
            kvTaskClient.put(SUBTASKS, allSubTasks);
        }
        try {
            if (getHistoryManager().getHistory() != null) {
                List<Integer> historyList = getHistory().stream()
                        .map(Task::getId)
                        .collect(Collectors.toList());
                String history = gson.toJson(historyList);
                kvTaskClient.put(HISTORY, history);
            }
        } catch (HistoryManagerException e) {
            e.getMessage();
        }
    }

    public void load() {
        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        try {
            ArrayList<Task> tasks = gson.fromJson(kvTaskClient.load(TASKS), taskType);
            tasks.forEach(this::addTask);
        } catch (Exception ex) {
            System.out.println("Проблемы при загрузке задач");
        }
        try {
            Type epicType = new TypeToken<ArrayList<Epic>>() {
            }.getType();
            ArrayList<Epic> epics = gson.fromJson(kvTaskClient.load(EPICS), epicType);
            epics.forEach(this::addEpic);
        } catch (Exception ex) {
            System.out.println("Проблемы при загрузке эпиков");
        }
        try {

            Type subTaskType = new TypeToken<ArrayList<Subtask>>() {
            }.getType();
            ArrayList<Subtask> subTasks = gson.fromJson(kvTaskClient.load(SUBTASKS), subTaskType);
            subTasks.forEach(this::addSubtask);
        } catch (Exception ex) {
            System.out.println("Проблемы при загрузке сабтасков");
        }

        Type historyType = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        ArrayList<Integer> history = gson.fromJson(kvTaskClient.load(HISTORY), historyType);
        for (Integer taskId : history) {
            getTaskById(taskId);
            getEpicById(taskId);
            getSubtaskById(taskId);

        }
    }

}