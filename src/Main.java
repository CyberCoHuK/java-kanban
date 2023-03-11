import manager.*;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;


public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Manager.getDefault();

        Task task1 = new Task("Задача 1", "Надо сделать доску");
        Task task2 = new Task("Задача 2", "Надеятся что она готова",
                LocalDateTime.of(2000, 1, 2, 0, 0),
                Duration.ofDays(3));


        taskManager.addTask(task1);
        taskManager.addTask(task2);


        Epic epic1 = new Epic("СуперЗадача 1", "Описание первого эпика");
        Epic epic2 = new Epic("СуперЗадача 2", "Описание второго эпика");

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        Subtask subtask1OfEpic1 = new Subtask("Подзадача 1 первого эпика",
                "Описание первой подзадачи",
                epic1.getId(),
                LocalDateTime.of(2000, 1, 6, 0, 0),
                Duration.ofDays(1));
        Subtask subtask2OfEpic1 = new Subtask("Подзадача 2 первого эпика",
                "Описание второй подзадачи",
                epic1.getId(),
                LocalDateTime.of(2000, 1, 1, 0, 0),
                Duration.ofHours(5));
        Subtask subtask3OfEpic1 = new Subtask("Подзадача 3 первого эпика",
                "Описание первой подзадачи",
                epic1.getId());

        taskManager.addSubtask(subtask1OfEpic1);
        taskManager.addSubtask(subtask2OfEpic1);
        taskManager.addSubtask(subtask3OfEpic1);
        TaskManager taskManager1 = FileBackedTasksManager.loadFromFile(new File("file.csv"));
        System.out.println(taskManager1.getTasks());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager1.getEpics());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager1.getSubtasks());
        System.out.println(taskManager.getSubtasks());
        Task task3 = new Task("Задача 3", "Надеятся что она готова",
                LocalDateTime.of(2000, 2, 1, 0, 0),
                Duration.ofDays(3));
        taskManager1.addTask(task3);
        System.out.println(taskManager1.getTasks());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasksOfEpicById(3));
        taskManager.removeSubtaskById(5);
        taskManager.removeTaskById(2);
        System.out.println(taskManager.getPrioritizedTasks());

        taskManager.removeAllSubtasks();
        System.out.println(taskManager.getPrioritizedTasks());

    }
}
