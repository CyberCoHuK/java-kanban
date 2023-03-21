package manager;

import exception.DateTimeValidateException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    public static Epic epic1;
    public static Epic epic2;
    public static Subtask subTask1;
    public static Subtask subTask2;
    public static Subtask subTask3;
    public static Task task1;
    public static Task task2;



    @Test
    void getTasks() {
        task1 = new Task("Task1", "Task1 description");
        task2 = new Task("Task2", "Task2 description");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertNotNull(taskManager.getTasks(), "Задачи не возвращаются");
        assertEquals(2, taskManager.getTasks().size(), "Неверное количество задач");
        assertEquals(task1, taskManager.getTasks().get(0), "Задачи не совпадают");
    }

    @Test
    void getEpics() {
        epic1 = new Epic("Epic1", "Epic1 description");
        epic2 = new Epic("Epic2", "Epic2 description");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        assertNotNull(taskManager.getEpics(), "Эпики не возвращаются");
        assertEquals(2, taskManager.getEpics().size(), "Неверное количество эпиков");
        assertEquals(epic1, taskManager.getEpics().get(0), "Эпики не совпадают");
    }

    @Test
    void getSubtasks() {
        epic1 = new Epic("Epic1", "Epic1 description");
        subTask1 = new Subtask("Subtask1", "Subtask1 description", 1);
        subTask2 = new Subtask("Subtask2", "Subtask3 description", 1);
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subTask1);
        taskManager.addSubtask(subTask2);
        assertNotNull(taskManager.getSubtasks(), "Эпики не возвращаются");
        assertEquals(2, taskManager.getSubtasks().size(), "Неверное количество эпиков");
        assertEquals(subTask1, taskManager.getSubtasks().get(0), "Эпики не совпадают");
    }

    @Test
    void removeAllTasks() {
        task1 = new Task("Task1", "Task1 description");
        task2 = new Task("Task2", "Task2 description");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.removeAllTasks();
        assertTrue(taskManager.getTasks().isEmpty(), "Лист тасков не очищен");
    }

    @Test
    void removeAllSubtasks() {
        epic1 = new Epic("Epic1", "Epic1 description");
        epic2 = new Epic("Epic2", "Epic2 description");
        subTask1 = new Subtask("Subtask1", "Subtask1 description", 1);
        subTask2 = new Subtask("Subtask2", "Subtask3 description", 1);
        subTask3 = new Subtask("Subtask3", "Subtask3 description", 2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubtask(subTask1);
        taskManager.addSubtask(subTask2);
        taskManager.addSubtask(subTask3);
        taskManager.removeAllSubtasks();
        assertTrue(taskManager.getSubtasks().isEmpty(), "Лист эпиков не очищен");
    }

    @Test
    void removeAllEpics() {
        epic1 = new Epic("Epic1", "Epic1 description");
        epic2 = new Epic("Epic2", "Epic2 description");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.removeAllEpics();
        assertTrue(taskManager.getEpics().isEmpty(), "Лист эпиков не очищен");
    }

    @Test
    void getTaskById() {
        assertNull(taskManager.getTaskById(1), "Задача не должна существовать");
        task1 = new Task("Task1", "Task1 description");
        taskManager.addTask(task1);
        assertEquals(taskManager.getTaskById(1), task1, "Задача не получена");
    }

    @Test
    void getSubtaskById() {
        assertNull(taskManager.getSubtaskById(2), "Подзадача не должна существовать");
        epic1 = new Epic("Epic1", "Epic1 description");
        subTask1 = new Subtask("Subtask1", "Subtask1 description", 1);
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subTask1);
        assertEquals(taskManager.getSubtaskById(2), subTask1, "Подзадача не получена");
    }

    @Test
    void getEpicById() {
        assertNull(taskManager.getEpicById(1), "Эпик не должен существовать");
        epic1 = new Epic("Epic1", "Epic1 description");
        taskManager.addEpic(epic1);
        assertEquals(taskManager.getEpicById(1), epic1, "Эпик не найден");
    }

    @Test
    void removeTaskById() {
        task1 = new Task("Task1", "Task1 description");
        taskManager.addTask(task1);
        taskManager.removeTaskById(1);
        assertTrue(taskManager.getTasks().isEmpty(), "Задача не удалена");
    }

    @Test
    void removeEpicById() {
        epic1 = new Epic("Epic1", "Epic1 description");
        taskManager.addEpic(epic1);
        taskManager.removeEpicById(1);
        assertNull(taskManager.getEpicById(1), "Эпик не удален");
    }

    @Test
    void removeSubtaskById() {
        epic1 = new Epic("Epic1", "Epic1 description");
        subTask1 = new Subtask("Subtask1", "Subtask1 description", 1);
        taskManager.removeSubtaskById(2);
        assertNull(taskManager.getSubtaskById(2), "Подзадача не удалена");
    }

    @Test
    void addTask() {
        task1 = new Task("Task1", "Task1 description");
        taskManager.addTask(task1);
        assertNotNull(taskManager.getTaskById(1), "Задача не найдена");
        assertEquals(task1, taskManager.getTaskById(1), "Задачи не совпадают");
    }

    @Test
    void addEpic() {
        epic1 = new Epic("Epic1", "Epic1 description");
        taskManager.addEpic(epic1);
        assertNotNull(taskManager.getEpicById(1), "Эпик не найдена");
        assertEquals(epic1, taskManager.getEpicById(1), "Эпики не совпадают");
    }

    @Test
    void addSubtask() {
        epic1 = new Epic("Epic1", "Epic1 description");
        subTask1 = new Subtask("Subtask1", "Subtask1 description", 1);
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subTask1);
        assertNotNull(taskManager.getSubtaskById(2), "Подзадача не найдена");
        assertEquals(subTask1, taskManager.getSubtaskById(2), "Подзадачи не совпадают");
    }

    @Test
    void updateTask() {
        Task taskTest = new Task(1, "Test", "Discription");
        taskManager.updateTask(taskTest);
        assertNull(taskManager.getTaskById(1), "Не должен обновится");
        task1 = new Task("Task1", "Task1 description");
        taskManager.addTask(task1);
        Task task1Upd = new Task(task1.getId(), "NewName", "new description");
        taskManager.updateTask(task1Upd);
        assertEquals(task1Upd, taskManager.getTaskById(1), "Задача не обновилась");
    }

    @Test
    void updateEpic() {
        Epic epicTest = new Epic(1, "Test", "Discription");
        taskManager.updateEpic(epicTest);
        assertNull(taskManager.getEpicById(1), "Не должен обновится");
        epic1 = new Epic("Epic1", "Epic1 description");
        taskManager.addEpic(epic1);
        Epic epic1Upd = new Epic(epic1.getId(), "NewName", "New description");
        taskManager.updateEpic(epic1Upd);
        assertEquals(epic1Upd, taskManager.getEpicById(1), taskManager.getEpics().toString());
    }

    @Test
    void updateSubtask() {
        Subtask subtaskTest = new Subtask(2, "Test", "Discription", Status.NEW, 1);
        taskManager.updateSubtask(subtaskTest);
        assertNull(taskManager.getSubtaskById(2), "Не должен обновится");
        epic1 = new Epic("Epic1", "Epic1 description");
        subTask1 = new Subtask("Subtask1", "Subtask1 description", 1);
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subTask1);
        Subtask subtask1Upd = new Subtask(subTask1.getId(), "NewName", "New description", Status.NEW, subTask1.getEpicId());
        taskManager.updateSubtask(subtask1Upd);
        assertEquals(subtask1Upd, taskManager.getSubtaskById(2), "Сабтаск не обновлен");
    }

    @Test
    void getSubtasksOfEpicById() {
        assertNull(taskManager.getSubtasksOfEpicById(1), "Подзадачи не должены существовать");
        epic1 = new Epic("Epic1", "Epic1 description");
        subTask1 = new Subtask("Subtask1", "Subtask1 description", 1);
        subTask2 = new Subtask("Subtask2", "Subtask3 description", 1);
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subTask1);
        taskManager.addSubtask(subTask2);
        assertNotNull(taskManager.getSubtasksOfEpicById(1), "Подзадачи эпика не найдены");
        assertEquals(2, taskManager.getSubtasksOfEpicById(1).size(), "Количество подзадач эпика не верно");
        assertEquals(List.of(taskManager.getSubtaskById(2), taskManager.getSubtaskById(3)), taskManager.getSubtasksOfEpicById(1), "Подзадачи эпика не сходятся");
    }

    @Test
    void validateTimeTest() {
        task1 = new Task("Task1", "Task1 description", LocalDateTime.of(2000, 1, 2, 0, 0),
                Duration.ofDays(3));
        task2 = new Task("Task2", "Task2 description", LocalDateTime.of(2000, 1, 2, 0, 0),
                Duration.ofDays(3));
        taskManager.addTask(task1);
        DateTimeValidateException ex = assertThrows(DateTimeValidateException.class, () -> taskManager.addTask(task2));
        assertEquals("Время пересекается с уже добавленной задачей", ex.getMessage());
    }
}