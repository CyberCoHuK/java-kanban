package manager;

import exception.*;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int nextId;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;
    private final HistoryManager history;
    private final TreeSet<Task> sortedTasks;
    private final List<Task> tasksWithoutDate;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        history = Manager.getDefaultHistory();
        sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        tasksWithoutDate = new ArrayList<>();
    }

    public HistoryManager getHistoryManager() {
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
            sortedTasks.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            history.removeFromHistory(subtask.getId());
            sortedTasks.remove(subtask);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasksIds().clear();
            calculateStatusOfEpic(epic);
            timeOfEpic(epic);
        }
    }

    @Override
    public void removeAllEpics() {
        for (Epic epic : epics.values()) {
            history.removeFromHistory(epic.getId());
            sortedTasks.remove(epic);
        }
        epics.clear();
        removeAllSubtasks();
    }

    @Override
    public Task getTaskById(Integer id) {
        if (tasks.get(id) != null) {
            history.addToHistory(tasks.get(id));
        }
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        if (subtasks.get(id) != null) {
            history.addToHistory(subtasks.get(id));
        }
        return subtasks.get(id);
    }

    @Override
    public Epic getEpicById(Integer id) {
        if (epics.get(id) != null) {
            history.addToHistory(epics.get(id));
        }
        return epics.get(id);
    }

    @Override
    public void removeTaskById(Integer id) {
        Task task = getTaskById(id);
        history.removeFromHistory(task.getId());
        sortedTasks.remove(task);
        tasks.remove(id);

    }

    @Override
    public void removeEpicById(Integer id) {
        Epic epic = epics.remove(id);
        history.removeFromHistory(epic.getId());
        sortedTasks.remove(epic);
        getSubtasksOfEpicById(epic.getId()).forEach(sortedTasks::remove);
        for (Integer subId : epic.getSubtasksIds()) {
            history.removeFromHistory(subId);
            subtasks.remove(subId);
        }
    }

    @Override
    public void removeSubtaskById(Integer id) {
        Subtask subtask = subtasks.remove(id);
        sortedTasks.remove(subtask);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasksIds().remove(id);
            calculateStatusOfEpic(epic);
            timeOfEpic(epic);
        }
    }

    @Override
    public void addTask(Task task) {
        while (true) {
            nextId++;
            if (!tasks.containsKey(nextId) && !epics.containsKey(nextId) && !subtasks.containsKey(nextId)) {
                task.setId(nextId);
                if (task.getStartTime() != null) {
                    if (validateTime(task)) {
                        sortedTasks.add(task);
                    } else {
                        throw new DateTimeValidateException("Время пересекается с уже добавленной задачей");
                    }
                } else {
                    tasksWithoutDate.add(task);
                }
                tasks.put(nextId, task);
                break;
            }
        }
    }

    @Override
    public void addEpic(Epic epic) {
        while (true) {
            nextId++;
            if (!tasks.containsKey(nextId) && !epics.containsKey(nextId) && !subtasks.containsKey(nextId)) {
                epic.setId(nextId);
                epics.put(nextId, epic);
                break;
            }
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        if (epics.containsKey(epicId)) {
            while (true) {
                nextId++;
                if (!tasks.containsKey(nextId) && !epics.containsKey(nextId) && !subtasks.containsKey(nextId)) {
                    subtask.setId(nextId);
                    boolean isStartTime = subtask.getStartTime() != null;
                    if (isStartTime) {
                        if (validateTime(subtask)) {
                            sortedTasks.add(subtask);
                        } else {
                            throw new DateTimeValidateException("Время пересекается с уже добавленной задачей");
                        }
                    } else {
                        tasksWithoutDate.add(subtask);
                    }
                    subtasks.put(nextId, subtask);
                    Epic epic = epics.get(epicId);
                    epic.addSubtaskId(nextId);
                    calculateStatusOfEpic(epic);
                    if (isStartTime) {
                        timeOfEpic(epic);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void calculateStatusOfEpic(Epic epic) {
        int newStatusCounter = 0;
        int doneStatusCounter = 0;
        ArrayList<Integer> subtasksId = epic.getSubtasksIds();
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
            if (subtask.getStartTime() != null) {
                timeOfEpic(epic);
            }
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpicById(Integer id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            final ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
            for (Integer subId : epic.getSubtasksIds()) {
                history.addToHistory(subtasks.get(subId));
                subtasksOfEpic.add(subtasks.get(subId));
            }
            return subtasksOfEpic;
        }
        return null;
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    private boolean validateTime(Task t) {
        if (sortedTasks.isEmpty()) {
            return true;
        }
        for (Task allTask : sortedTasks) {
            if ((t.getStartTime().isEqual(allTask.getStartTime()) || t.getStartTime().isAfter(allTask.getStartTime())) &&
                    (t.getStartTime().isBefore(allTask.getEndTime()) || t.getStartTime().isEqual(allTask.getEndTime())) ||
                    (t.getEndTime().isAfter(allTask.getStartTime()) || t.getEndTime().isEqual(allTask.getStartTime())) &&
                            (t.getEndTime().isBefore(allTask.getEndTime()) || t.getEndTime().isEqual(allTask.getEndTime()))) {
                return false;
            }
        }
        return true;
    }

    public List<Task> getPrioritizedTasks() {
        List<Task> result = new ArrayList<>(sortedTasks);
        result.addAll(tasksWithoutDate);
        return result;
    }

    private void timeOfEpic(Epic epic) {
        TreeSet<Subtask> sortedSubtask = new TreeSet<>(Comparator.comparing(Subtask::getStartTime));
        for (Subtask subtask : getSubtasksOfEpicById(epic.getId())) {
            if (subtask.getStartTime() != null) {
                sortedSubtask.add(subtask);
            }
        }
        if (!sortedSubtask.isEmpty()) {
            LocalDateTime startOfEpic = sortedSubtask.first().getStartTime();
            epic.setStartTime(startOfEpic);
            LocalDateTime endOfEpic = sortedSubtask.last().getEndTime();
            Duration duration = Duration.between(startOfEpic, endOfEpic);
            epic.setDuration(duration);
        } else {
            epic.setStartTime(null);
            epic.setDuration(null);
        }
    }

    protected HashMap<Integer, Epic> getMapEpic() {
        return epics;
    }

    protected HashMap<Integer, Subtask> getMapSubtask() {
        return subtasks;
    }

    protected HashMap<Integer, Task> getMapTask() {
        return tasks;
    }
}
