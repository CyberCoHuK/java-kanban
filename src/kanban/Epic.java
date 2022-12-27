package kanban;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId;

    public Epic(String name, String description) {
        super(name, description);
        subtasksId = new ArrayList<>();
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);
        subtasksId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void addSubtaskId(Integer id) {
        subtasksId.add(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskId=" + subtasksId +
                '}';
    }
}
