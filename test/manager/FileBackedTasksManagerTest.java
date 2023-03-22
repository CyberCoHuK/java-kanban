package manager;

import exception.ManagerLoadException;
import exception.ManagerSaveException;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    File file;

    @BeforeEach
    public void setManager() {
        taskManager = new FileBackedTasksManager("test.csv");
    }


    @Test
    void epicWithoutSubtasks() throws IOException {
        file = new File("test2.csv");
        FileWriter writer = new FileWriter(file);
        writer.write("id,type,name,status,description,startTime,endTime,duration,epic,");
        writer.write("\n1,EPIC,Epic1,NEW,Epic1 description");
        writer.write("\n2,SUBTASK,Subtask1,NEW,Subtask1 description,1");
        writer.close();
        TaskManager taskManager1 = FileBackedTasksManager.loadFromFile(file);
        assertEquals(1, taskManager1.getEpics().size(), "Неверное количество подзадач");
        taskManager1.removeSubtaskById(subTask1.getId());
        int a = 0;
        if (taskManager1.getSubtasksOfEpicById(1) != null) {
            a = taskManager1.getSubtasksOfEpicById(1).size();
        }
        assertEquals(0, a, "Неверное количество подзадач");
    }

    @Test
    void loadWrongFileName() {
        ManagerLoadException ex = assertThrows(ManagerLoadException.class, () -> FileBackedTasksManager.loadFromFile(new File("elif.csv")));
        assertEquals("Ошибка загрузки файла elif.csv (Не удается найти указанный файл)", ex.getMessage());
    }

    @Test
    void saveWrongFileName() {
        ManagerSaveException ex = assertThrows(ManagerSaveException.class, () -> {
            TaskManager taskManager1 = new FileBackedTasksManager("&?asdf");
            Task task = new Task("test", "test");
            taskManager1.addTask(task);
        });
        assertEquals("Ошибка записи файла&?asdf (Синтаксическая ошибка в имени файла, имени папки или метке тома)", ex.getMessage());
    }
}