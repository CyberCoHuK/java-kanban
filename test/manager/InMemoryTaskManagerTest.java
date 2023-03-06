package manager;

class InMemoryTaskManagerTest extends TaskManagerTest {
    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }
}