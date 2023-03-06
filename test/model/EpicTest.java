package model;

import static org.junit.jupiter.api.Assertions.*;

import manager.Manager;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpicTest {
    Epic epic;
    TaskManager taskManager;

    @BeforeEach
    void BeforeEach() {
        taskManager = Manager.getDefault();
        epic = new Epic("Epic", "Epic description");
        taskManager.addEpic(epic);
    }
    @Test
    void CheckEmptyListOfSubtasks() {
        boolean list = taskManager.getSubtasks().isEmpty();
        assertTrue(list);
    }
    @Test
    void CheckAllSubtaskWithStatusNEW(){
        Subtask subtask1 = new Subtask("Subtask1", "Subtask description",  1);
        Subtask subtask2 = new Subtask("Subtask2", "Subtask description",  1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        Assertions.assertEquals(epic.getStatus(), Status.NEW);
    }
    @Test
    void CheckAllSubtaskWithStatusDONE(){
        Subtask subtask1 = new Subtask("Subtask1", "Subtask description",  1);
        Subtask subtask2 = new Subtask("Subtask2", "Subtask description",  1);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(epic.getStatus(), Status.DONE);
    }
    @Test
    void CheckSubtaskWithStatusNEWandDONE(){
        Subtask subtask1 = new Subtask("Subtask1", "Subtask description",  1);
        Subtask subtask2 = new Subtask("Subtask2", "Subtask description",  1);
        subtask1.setStatus(Status.DONE);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }
    @Test
    void CheckSubtaskWithStatusINPROGRESS(){
        Subtask subtask1 = new Subtask("Subtask1", "Subtask description",  1);
        Subtask subtask2 = new Subtask("Subtask2", "Subtask description",  1);
        subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }
}