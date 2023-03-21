import manager.*;
import model.Epic;
import model.Subtask;
import model.Task;
import server.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;


public class Main {

    public static void main(String[] args) throws IOException {

        KVServer kvServer = new KVServer();
        kvServer.start();
        TaskManager taskManager = Manager.getDefault();
        //Tests
        Epic epic1 = new Epic("Epic1", "Epic description1");
        taskManager.addEpic(epic1);
        Subtask subtask1OfEpic1 = new Subtask("Подзадача 1 первого эпика",
                "Описание первой подзадачи",
                1,
                LocalDateTime.of(2000, 1, 6, 0, 0),
                Duration.ofDays(1));
        Subtask subtask2OfEpic1 = new Subtask("Подзадача 2 первого эпика",
                "Описание второй подзадачи",
                1,
                LocalDateTime.of(2000, 1, 1, 0, 0),
                Duration.ofHours(5));
        Subtask subtask3OfEpic1 = new Subtask("Подзадача 3 первого эпика",
                "Описание первой подзадачи",
                1);
        taskManager.addSubtask(subtask1OfEpic1);
        taskManager.addSubtask(subtask2OfEpic1);
        taskManager.addSubtask(subtask3OfEpic1);

        Task task1 = new Task("Задача 1", "Надо сделать доску");
        Task task2 = new Task("Задача 2", "Надеятся что она готова",
                LocalDateTime.of(2000, 1, 2, 0, 0),
                Duration.ofDays(3));
        Task task3 = new Task("Задача 3", "Надо сделать доску");
        Task task4 = new Task("Задача 4", "Надеятся что она готова",
                LocalDateTime.of(2001, 1, 2, 0, 0),
                Duration.ofDays(3));
        taskManager.getEpicById(1);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addTask(task4);
        Epic epic2 = new Epic("Epic2", "Epic description2");
        taskManager.addEpic(epic2);
        taskManager.getEpicById(6);
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getHistory());
        kvServer.stop();
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.startServer();
        httpTaskServer.stopServer(1);
    }
}

