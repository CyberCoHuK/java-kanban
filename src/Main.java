import manager.*;
import model.Epic;
import model.Subtask;
import model.Task;
import server.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;


public class Main {

    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        TaskManager taskManager = Manager.getDefault();
        //Tests
        Task task1 = new Task("Задача 1", "Надо сделать доску");
        Task task2 = new Task("Задача 2", "Надеятся что она готова",
                LocalDateTime.of(2000, 1, 2, 0, 0),
                Duration.ofDays(3));

        Epic epic1 = new Epic("СуперЗадача 1", "Описание первого эпика");


        taskManager.addEpic(epic1);


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
        HttpTaskManager httpTaskManager = new HttpTaskManager(8078);
        taskManager.getEpicById(1);
        httpTaskManager.load();
        System.out.println("*********!");
        System.out.println(httpTaskManager.getHistory());
        System.out.println("*********!");
        taskManager.addTask(task1);
        taskManager.addTask(task2);


        Epic epic2 = new Epic("СуперЗадача 2", "Описание второго эпика");
        taskManager.addEpic(epic2);

        taskManager.getEpicById(2);
        taskManager.getEpicById(1);
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskById(3);
        taskManager.getSubtaskById(5);
        FileBackedTasksManager taskManager2 = FileBackedTasksManager.loadFromFile(new File("file.csv"));
        System.out.println(taskManager.getHistory());
        System.out.println("*********");
        System.out.println(taskManager2.getHistory());
        System.out.println("*********");
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(3);
        System.out.println(taskManager.getHistory());
        taskManager.getEpicById(1);
        System.out.println(taskManager.getHistory());
        taskManager2.removeSubtaskById(4);
        taskManager.removeSubtaskById(4);
        System.out.println(taskManager.getHistory());
        taskManager.removeEpicById(1);
        System.out.println(taskManager.getHistory());
        //kvServer.stop();
    }
}

