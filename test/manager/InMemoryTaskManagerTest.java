package manager;

import org.junit.jupiter.api.BeforeEach;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setManager() {
        taskManager = new InMemoryTaskManager();
    }
}