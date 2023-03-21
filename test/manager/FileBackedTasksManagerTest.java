package manager;

import exception.ManagerLoadException;
import exception.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void setManager() {
        taskManager = new FileBackedTasksManager("test.csv");
    }


    @Test
    void EpicWithoutSubtasks() {
        TaskManager taskManager1 = FileBackedTasksManager.loadFromFile(new File("file.csv"));
        epic1 = new Epic("Epic1", "Epic1 description");
        subTask1 = new Subtask("Subtask1", "Subtask1 description", 1);
        taskManager1.addEpic(epic1);
        taskManager1.addSubtask(subTask1);
        taskManager1.removeSubtaskById(subTask1.getId());
        int a = 0;
        if(taskManager1.getSubtasksOfEpicById(1)!=null) {
            a = taskManager1.getSubtasksOfEpicById(1).size();
        }
        assertEquals(0, a, "Неверное количество подзадач");
    }

    @Test
    void LoadWrongFileName() {
        ManagerLoadException ex = assertThrows(ManagerLoadException.class, () -> FileBackedTasksManager.loadFromFile(new File("elif.csv")));
        assertEquals("Ошибка загрузки файла elif.csv (Не удается найти указанный файл)", ex.getMessage());
    }

    @Test
    void SaveWrongFileName() {
        ManagerSaveException ex = assertThrows(ManagerSaveException.class, () -> {
            TaskManager taskManager1 = new FileBackedTasksManager("&?asdf");
            Task task = new Task("test", "test");
            taskManager1.addTask(task);
        });
        assertEquals("Ошибка записи файла&?asdf (Синтаксическая ошибка в имени файла, имени папки или метке тома)", ex.getMessage());
    }
}