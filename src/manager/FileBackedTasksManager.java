package manager;

import exception.ManagerLoadException;
import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private final String fileName;
    private static final String HEADER = "id,type,name,status,description,startTime,endTime,duration,epic,";

    public FileBackedTasksManager(String fileName) {
        this.fileName = fileName;
    }

    public void save() {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName))) {
            fileWriter.write(HEADER + "\n");
            if (getTasks().size() > 0) {
                for (Task task : super.getTasks()) {
                    fileWriter.write(task.toString() + "\n");
                }
            }
            if (getEpics().size() > 0) {
                for (Task epic : super.getEpics()) {
                    fileWriter.write(epic.toString() + "\n");
                }
            }
            if (getSubtasks().size() > 0) {
                for (Task subtask : super.getSubtasks()) {
                    fileWriter.write(subtask.toString() + "\n");
                }
            }
            if (getHistoryManager().getHistory() != null) {
                fileWriter.write("\n");
                ArrayList<String> id = new ArrayList<>();
                for (Task task : getHistory()) {
                    id.add(String.valueOf(task.getId()));
                }
                fileWriter.write(String.join(",", id));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла" + e.getMessage());
        }
    }

    private static Task fromString(String value) throws NullPointerException {
        final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");
        String[] taskData = value.split(",");
        final int id = Integer.parseInt(taskData[0]);
        final Type type = Type.valueOf(taskData[1]);
        final String name = taskData[2];
        final Status status = Status.valueOf(taskData[3]);
        final String description = taskData[4];
        LocalDateTime startTime = null;
        Duration duration = null;
        if (taskData.length > 6) {
            startTime = LocalDateTime.parse(taskData[5], FORMATTER);
            duration = Duration.parse(taskData[7]);
        }

        switch (type) {
            case TASK:
                return new Task(id, name, description, status, startTime, duration);
            case SUBTASK:
                int epicId;
                if (taskData.length > 6) {
                    epicId = Integer.parseInt(taskData[8]);
                } else {
                    epicId = Integer.parseInt(taskData[5]);
                }
                return new Subtask(id, name, description, status, epicId, startTime, duration);
            case EPIC:
                return new Epic(id, name, description, status);
            default:
                throw new NullPointerException();
        }
    }

    private static List<Integer> historyFromString(String history) {
        String[] values = history.split(",");
        List<Integer> result = new ArrayList<>();
        for (String val : values) {
            result.add(Integer.valueOf(val));
        }
        return result;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file.getPath());
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            while (fileReader.ready()) {
                String line = fileReader.readLine();
                if (line.equals(HEADER)) continue;
                if (!line.isEmpty()) {
                    Task task = fromString(line);
                    if (task instanceof Epic) {
                        manager.getMapEpic().put(task.getId(), (Epic) task);

                    } else if (task instanceof Subtask) {
                        manager.getMapSubtask().put(task.getId(), (Subtask) task);
                    } else {
                        manager.getMapTask().put(task.getId(), task);
                    }
                } else {
                    line = fileReader.readLine();
                    for (Integer id : historyFromString(line)) {
                        manager.getTaskById(id);
                        manager.getEpicById(id);
                        manager.getSubtaskById(id);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка загрузки файла " + e.getMessage());
        }
        return manager;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeTaskById(Integer id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(Integer id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(Integer id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Task getTaskById(Integer id) {
        if (super.getTaskById(id) != null) {
            save();
        }
        return super.getTaskById(id);
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        if (super.getSubtaskById(id) != null) {
            save();
        }
        return super.getSubtaskById(id);
    }

    @Override
    public Epic getEpicById(Integer id) {
        if (super.getEpicById(id) != null) {
            save();
        }
        return super.getEpicById(id);
    }
}
