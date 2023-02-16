import manager.*;
import model.*;

import java.io.File;


public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Manager.getDefault();

        Task task1 = new Task("Задача 1", "Надо сделать доску");
        Task task2 = new Task("Задача 2", "Надеятся что она готова");

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("СуперЗадача 1", "Описание первого эпика");
        Epic epic2 = new Epic("СуперЗадача 2", "Описание второго эпика");

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        Subtask subtask1OfEpic1 = new Subtask("Подзадача 1 первого эпика",
                "Описание первой подзадачи",
                epic1.getId());
        Subtask subtask2OfEpic1 = new Subtask("Подзадача 2 первого эпика",
                "Описание второй подзадачи",
                epic1.getId());
        Subtask subtask3OfEpic1 = new Subtask("Подзадача 3 первого эпика",
                "Описание первой подзадачи",
                epic1.getId());

        taskManager.addSubtask(subtask1OfEpic1);
        taskManager.addSubtask(subtask2OfEpic1);
        taskManager.addSubtask(subtask3OfEpic1);
        System.out.println(task1);
        System.out.println(epic1);
        System.out.println(subtask1OfEpic1);

        Subtask subtask1OfEpic2 = new Subtask("Подзадача 1 второго эпика",
                "Описание первой подзадачи",
                epic2.getId());
        taskManager.addSubtask(subtask1OfEpic2);
        Task task3 = new Task("Задача 3", "Надеятся что она готова");

        taskManager.addTask(task3);
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        taskManager.getTaskById(2);
        TaskManager taskManager1 = FileBackedTasksManager.loadFromFile(new File("file.csv"));
        System.out.println(taskManager1.getTasks());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager1.getEpics());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager1.getSubtasks());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager1.getHistory());
        System.out.println(taskManager.getHistory());
    }
}
