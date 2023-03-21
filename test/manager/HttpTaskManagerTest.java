package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;
import model.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    KVServer kvServer;

    @BeforeEach
    void setUp() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = new HttpTaskManager(8078);
    }

    @AfterEach
    void tearDown() {
        kvServer.stop();
    }

    @Test
    void save() {
        Epic epic1 = new Epic("Epic1", "Epic description1");
        Subtask subTask1 = new Subtask("SubTask1", "Subtask description1", 1,
                LocalDateTime.of(2022, 8, 3, 18, 33), Duration.ofMinutes(15));
        Subtask subTask2 = new Subtask("SubTask2", "Subtask description2", 1,
                LocalDateTime.of(2022, 8, 3, 18, 50), Duration.ofMinutes(15));
        Subtask subTask3 = new Subtask("SubTask3", "Subtask description3", 1,
                LocalDateTime.of(2022, 8, 3, 19, 50), Duration.ofMinutes(15));
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subTask1);
        taskManager.addSubtask(subTask2);
        taskManager.addSubtask(subTask3);
        final List<Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size(), "Неверное количество эпиков.");

    }

    @Test
    void load() {
        Epic epic1 = new Epic("Epic1", "Epic description1");
        Subtask subTask1 = new Subtask("SubTask1", "Subtask description1", 1,
                LocalDateTime.of(2022, 7, 3, 18, 33), Duration.ofMinutes(15));
        Subtask subTask2 = new Subtask("SubTask2", "Subtask description2", 1,
                LocalDateTime.of(2022, 8, 3, 18, 50), Duration.ofMinutes(15));
        Subtask subTask3 = new Subtask("SubTask3", "Subtask description3", 1,
                LocalDateTime.of(2022, 9, 3, 19, 50), Duration.ofMinutes(15));
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subTask1);
        taskManager.addSubtask(subTask2);
        taskManager.addSubtask(subTask3);
        taskManager.getEpicById(1);
        HttpTaskManager httpTaskManager = new HttpTaskManager(8078);
        httpTaskManager.load();
        final List<Subtask> subTasks = httpTaskManager.getSubtasksOfEpicById(epic1.getId());
        assertEquals(3, subTasks.size(), "Неверное количество эпиков.");
    }
}