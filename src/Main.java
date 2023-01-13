import manager.*;
import model.*;


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
        Subtask subtask1OfEpic2 = new Subtask("Подзадача 1 второго эпика",
                "Описание первой подзадачи",
                epic2.getId());

        taskManager.addSubtask(subtask1OfEpic1);
        taskManager.addSubtask(subtask2OfEpic1);
        taskManager.addSubtask(subtask1OfEpic2);
        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.getEpicById(1));
        System.out.println(taskManager.getEpicById(2));
        System.out.println(taskManager.getSubtaskById(1));
        System.out.println(taskManager.getSubtaskById(2));
        System.out.println(taskManager.getSubtaskById(3));
        System.out.println(taskManager.getSubtasksOfEpicById(1));
        System.out.println(taskManager.getHistory());
    }
}
