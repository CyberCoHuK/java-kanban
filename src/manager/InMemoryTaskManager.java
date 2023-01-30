package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int nextTaskId;
    private int nextSubtaskId;
    private int nextEpicId;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;
    private final HistoryManager history;

    public InMemoryTaskManager() {
        nextTaskId = 1;
        nextSubtaskId = 1;
        nextEpicId = 1;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        history = Manager.getDefaultHistory();
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void removeAllTasks() {
        for (Task task : tasks.values()) {
            history.removeFromHistory(task.hashCode());
        }
        tasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            history.removeFromHistory(subtask.hashCode());
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasksId().clear();
            calculateStatusOfEpic(epic);
        }
    }

    @Override
    public void removeAllEpics() {
        for (Epic epic : epics.values()) {
            history.removeFromHistory(epic.hashCode());
        }
        epics.clear();
        removeAllSubtasks();
    }

    @Override
    public Task getTaskById(Integer id) {
        history.addToHistory(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        history.addToHistory(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpicById(Integer id) {
        history.addToHistory(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void removeTaskById(Integer id) {
        Task task = getTaskById(id);
        history.removeFromHistory(task.hashCode());
        tasks.remove(id);

    }

    @Override
    public void removeEpicById(Integer id) {
        Epic epic = epics.remove(id);
        history.removeFromHistory(epic.hashCode());
        for (Integer subId : epic.getSubtasksId()) {
            Subtask subtask = subtasks.get(subId);
            history.removeFromHistory(subtask.hashCode());
            subtasks.remove(subId);
        }
    }

    @Override
    public void removeSubtaskById(Integer id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasksId().remove(id);
            calculateStatusOfEpic(epic);
        }
    }

    @Override
    public void addTask(Task task) {
        task.setId(getNextTaskId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(getNextEpicId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        if (epics.containsKey(epicId)) {
            int subtaskId = getNextSubtaskId();
            subtask.setId(subtaskId);
            subtasks.put(subtaskId, subtask);
            Epic epic = epics.get(epicId);
            epic.addSubtaskId(subtaskId);
            calculateStatusOfEpic(epic);
        }
    }

    @Override
    public void calculateStatusOfEpic(Epic epic) {
        int newStatusCounter = 0;
        int doneStatusCounter = 0;
        ArrayList<Integer> subtasksId = epic.getSubtasksId();
        for (Integer subtaskId : subtasksId) {
            Status statusOfSubtask = subtasks.get(subtaskId).getStatus();
            if (statusOfSubtask == Status.NEW) {
                newStatusCounter++;
            } else if (statusOfSubtask == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else if (statusOfSubtask == Status.DONE) {
                doneStatusCounter++;
            }
        }
        if (newStatusCounter == subtasksId.size()) {
            epic.setStatus(Status.NEW);
        } else if (doneStatusCounter == subtasksId.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void updateTask(Task task) {
        tasks.replace(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.replace(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            subtasks.replace(subtask.getId(), subtask);
            calculateStatusOfEpic(epic);
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpicById(Integer id) {
        Epic epic = epics.get(id);
        final ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        for (Integer subId : epic.getSubtasksId()) {
            history.addToHistory(subtasks.get(subId));
            subtasksOfEpic.add(subtasks.get(subId));
        }
        return subtasksOfEpic;
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    private int getNextTaskId() {
        return nextTaskId++;
    }

    private int getNextSubtaskId() {
        return nextSubtaskId++;
    }

    private int getNextEpicId() {
        return nextEpicId++;
    }
}
