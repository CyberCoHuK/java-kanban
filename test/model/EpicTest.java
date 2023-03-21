package model;

import static org.junit.jupiter.api.Assertions.*;

import manager.Manager;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpicTest {
    private Epic epic;
    private TaskManager taskManager;
    private Subtask subtask1;
    private Subtask subtask2;

    @BeforeEach
    void BeforeEach() {
        taskManager = Manager.getFileBacked();
        epic = new Epic("Epic", "Epic description");
        taskManager.addEpic(epic);
        subtask1 = new Subtask("Subtask1", "Subtask description", 1);
        subtask2 = new Subtask("Subtask2", "Subtask description", 1);
    }

    @Test
    void CheckEmptyListOfSubtasks() {
        boolean list = taskManager.getSubtasks().isEmpty();
        assertTrue(list);
    }

    @Test
    void CheckAllSubtaskWithStatusNEW() {
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        Assertions.assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    void CheckAllSubtaskWithStatusDONE() {
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(epic.getStatus(), Status.DONE, epic.getSubtasksIds().toString());
    }

    @Test
    void CheckSubtaskWithStatusNEWandDONE() {
        subtask1.setStatus(Status.DONE);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void CheckSubtaskWithStatusINPROGRESS() {
        subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }
}