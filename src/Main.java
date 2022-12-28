import manager.Manager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        printLine();
        System.out.println("Тестирование задач\n");

        Task task1 = new Task("Задача 1", "Надо сделать доску");
        Task task2 = new Task("Задача 2", "Надеятся что она готова");

        manager.addTask(task1);
        manager.addTask(task2);

        System.out.println(manager.getTasks());

        Task updatedTask1 = new Task(task1.getId(),
                "Уверен что Задача 1",
                "И все еще надо сделать доску",
                Status.IN_PROGRESS);
        Task updatedTask2 = new Task(task2.getId(),
                "Уверен что Задача 2",
                "И все еще надеятся что она готова",
                Status.IN_PROGRESS);

        manager.updateTask(updatedTask1);
        manager.updateTask(updatedTask2);

        System.out.println(manager.getTasks());

        manager.removeTaskById(2);

        System.out.println(manager.getTasks());
        System.out.println(manager.getTaskById(1));

        manager.removeAllTasks();

        System.out.println(manager.getTasks());

        printLine();
        System.out.println("Тестирование эпиков\n");

        Epic epic1 = new Epic("СуперЗадача 1", "Описание первого эпика");
        Epic epic2 = new Epic("СуперЗадача 2", "Описание второго эпика");

        manager.addEpic(epic1);
        manager.addEpic(epic2);

        Subtask subtask1OfEpic1 = new Subtask("Подзадача 1 первого эпика",
                "Описание первой подзадачи",
                epic1.getId());
        Subtask subtask2OfEpic1 = new Subtask("Подзадача 2 первого эпика",
                "Описание второй подзадачи",
                epic1.getId());
        Subtask subtask1OfEpic2 = new Subtask("Подзадача 1 второго эпика",
                "Описание первой подзадачи",
                epic1.getId());

        manager.addSubtask(subtask1OfEpic1);
        manager.addSubtask(subtask2OfEpic1);
        manager.addSubtask(subtask1OfEpic2);

        System.out.println(manager.getEpics());

        Subtask updateSubtask1OfEpic1 = new Subtask(
                subtask1OfEpic1.getId(),
                "Новое имя первой подзадачи первого эпика",
                "Новое описание",
                Status.IN_PROGRESS,
                epic1.getId()
        );

        manager.updateSubtask(updateSubtask1OfEpic1);

        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getSubtaskById(1));

        Subtask updateSubtask1OfEpic2 = new Subtask(
                subtask1OfEpic2.getId(),
                "Новое имя первой подзадачи второго эпика",
                "Новое описание",
                Status.DONE,
                epic2.getId()
        );

        manager.updateSubtask(updateSubtask1OfEpic2);

        System.out.println(manager.getEpicById(2));

        printLine();
        System.out.println("Тестирование подзадач\n");

        Epic updatedEpic2 = new Epic(epic2.getId(), "Обновление второго эпика", "Новое описание эпика");

        manager.updateEpic(updatedEpic2);
        System.out.println(manager.getEpicById(2));
        manager.removeEpicById(2);
        System.out.println(manager.getSubtasks());

        manager.removeSubtaskById(subtask1OfEpic1.getId());

        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getSubtasksOfEpicById(1));

        manager.removeAllSubtasks();
        manager.removeAllEpics();
        manager.removeAllTasks();

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
    }

    private static void printLine() {
        System.out.println("\n----------------------------------------\n");
    }
}
