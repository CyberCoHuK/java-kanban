package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int nextTaskId;
    private int nextSubtaskId;
    private int nextEpicId;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subtasks;
    private HashMap<Integer, Epic> epics;

    public Manager() {
        nextTaskId = 1;
        nextSubtaskId = 1;
        nextEpicId = 1;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasksId().clear();
            calculateStatusOfEpic(epic);
        }
    }

    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public Task getTaskById(Integer id) {
        return tasks.get(id);
    }

    public Task getSubtaskById(Integer id) {
        return subtasks.get(id);
    }

    public Task getEpicById(Integer id) {
        return epics.get(id);
    }

    public void removeTaskById(Integer id) {
        tasks.remove(id);
    }

    public void removeEpicById(Integer id) {
        Epic epic = epics.remove(id);
        for (Integer subId : epic.getSubtasksId()) {
            subtasks.remove(subId);
        }
    }

    public void removeSubtaskById(Integer id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasksId().remove(id);
            calculateStatusOfEpic(epic);
        }
    }

    public void addTask(Task task) {
        task.setId(getNextTaskId());
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setId(getNextEpicId());
        epics.put(epic.getId(), epic);
    }

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

    private void calculateStatusOfEpic(Epic epic) {
        int newStatusCounter = 0;
        int doneStatusCounter = 0;
        ArrayList<Integer> subtasksId = epic.getSubtasksId();
        for (Integer subtaskId : subtasksId) {
            Status statusOfSubtask = subtasks.get(subtaskId).getStatus();
            if (statusOfSubtask.equals(Status.NEW)) {
                newStatusCounter++;
            } else if (statusOfSubtask.equals(Status.IN_PROGRESS)) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else if (statusOfSubtask.equals(Status.DONE)) {
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

    public void updateTask(Task task) {
        tasks.replace(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.replace(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            subtasks.replace(subtask.getId(), subtask);
            calculateStatusOfEpic(epic);
        }
    }

    public ArrayList<Subtask> getSubtasksOfEpicById(Integer id) {
        Epic epic = epics.get(id);
        final ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        for (Integer subId : epic.getSubtasksId()) {
            subtasksOfEpic.add(subtasks.get(subId));
        }
        return subtasksOfEpic;
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
