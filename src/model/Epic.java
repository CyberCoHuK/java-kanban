package model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void addSubtaskId(Integer id) {
        subtasksIds.add(id);
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
