package manager;

import model.*;

import java.util.ArrayList;

public interface TaskManager {

    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

    void removeAllTasks();

    void removeAllSubtasks();

    void removeAllEpics();

    Task getTaskById(Integer id);

    Subtask getSubtaskById(Integer id);

    Epic getEpicById(Integer id);

    void removeTaskById(Integer id);

    void removeEpicById(Integer id);

    void removeSubtaskById(Integer id);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void calculateStatusOfEpic(Epic epic);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    ArrayList<Subtask> getSubtasksOfEpicById(Integer id);

    ArrayList<Task> getHistory();
}
