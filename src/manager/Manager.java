package manager;

import kanban.*;

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

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public int getNextTaskId() {
        return nextTaskId++;
    }

    public int getNextSubtaskId() {
        return nextSubtaskId++;
    }

    public int getNextEpicId() {
        return nextEpicId++;
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllSubtasks() {
        subtasks.clear();
    }

    public void removeAllEpics() {
        epics.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Task getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public Task getEpicById(int id) {
        return epics.get(id);
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeEpicById(int id) {
        epics.remove(id);
    }

    public void removeSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasksId().remove(id);
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
            epic.setStatus(calculateStatusOfEpic(epic));
        }
    }

    private Status calculateStatusOfEpic(Epic epic) {
        int newStatusCounter = 0;
        int inProgressStatusCounter = 0;
        int doneStatusCounter = 0;
        ArrayList<Integer> subtasksId = epic.getSubtasksId();
        for (Integer subtaskId : subtasksId) {
            Status statusOfSubtask = subtasks.get(subtaskId).getStatus();
            if (statusOfSubtask.equals(Status.NEW)) {
                newStatusCounter++;
            } else if (statusOfSubtask.equals(Status.IN_PROGRESS)) {
                inProgressStatusCounter++;
            } else if (statusOfSubtask.equals(Status.DONE)) {
                doneStatusCounter++;
            }
        }
        if (newStatusCounter == subtasksId.size()) {
            return Status.NEW;
        } else if (inProgressStatusCounter > 0) {
            return Status.IN_PROGRESS;
        } else if (doneStatusCounter == subtasksId.size()) {
            return Status.DONE;
        }
        return null;
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
            epic.setStatus(calculateStatusOfEpic(epic));
        }
    }

    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        final ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        for (Integer id : epic.getSubtasksId()) {
            subtasksOfEpic.add(subtasks.get(id));
        }
        return subtasksOfEpic;
    }
}
