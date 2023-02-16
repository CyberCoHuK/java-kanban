package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int nextId;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;
    private final HistoryManager history;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        history = Manager.getDefaultHistory();
    }
    public HistoryManager getHistoryManager(){
        return history;
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
            history.removeFromHistory(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            history.removeFromHistory(subtask.getId());
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
            history.removeFromHistory(epic.getId());
        }
        epics.clear();
        removeAllSubtasks();
    }

    @Override
    public Task getTaskById(Integer id) {
        if (tasks.get(id) != null){
            history.addToHistory(tasks.get(id));
        }
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        if (subtasks.get(id) != null){
            history.addToHistory(subtasks.get(id));
        }
        return subtasks.get(id);
    }

    @Override
    public Epic getEpicById(Integer id) {
        if(epics.get(id) != null){
            history.addToHistory(epics.get(id));
        }
        return epics.get(id);
    }

    @Override
    public void removeTaskById(Integer id) {
        Task task = getTaskById(id);
        history.removeFromHistory(task.getId());
        tasks.remove(id);

    }

    @Override
    public void removeEpicById(Integer id) {
        Epic epic = epics.remove(id);
        history.removeFromHistory(epic.getId());
        for (Integer subId : epic.getSubtasksId()) {
            history.removeFromHistory(subId);
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
        nextId++;
        task.setId(nextId);
        tasks.put(nextId, task);
    }

    @Override
    public void addEpic(Epic epic) {
        nextId++;
        epic.setId(nextId);
        epics.put(nextId, epic);
    }
    @Override
    public void addSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        if (epics.containsKey(epicId)) {
            nextId++;
            subtask.setId(nextId);
            subtasks.put(nextId, subtask);
            Epic epic = epics.get(epicId);
            epic.addSubtaskId(nextId);
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

    protected HashMap<Integer, Epic> getMapEpic(){
        return epics;
    }
    protected HashMap<Integer, Subtask> getMapSubtask(){
        return subtasks;
    }
    protected HashMap<Integer, Task> getMapTask(){
        return tasks;
    }
}
