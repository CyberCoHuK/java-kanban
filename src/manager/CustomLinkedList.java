package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;

    private Map<Integer, Node<T>> nodeMap = new HashMap<>();

    protected void linkLast(T task) {
        final Node<T> oldTail = tail;
        final Node<T> newNode = new Node<>(tail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            newNode.getPrev().setNext(newNode);
        }
        nodeMap.put(task.hashCode(), newNode);
    }

    protected boolean contains(T task) {
        return nodeMap.containsKey(task.hashCode());
    }

    protected void removeNode(Node<T> node) {
        if (node == null) {
            return;
        }
        Node<T> prev = node.getPrev();
        Node<T> next = node.getNext();
        if (node.equals(head) && !node.equals(tail)) {
            if (next != null) {
                next.setPrev(null);
            }
            head = next;
        } else if (node.equals(tail) && !node.equals(head)) {
            prev.setNext(null);
        } else if (!node.equals(head) && !node.equals(tail)) {
            prev.setNext(next);
            if (next != null) {
                next.setPrev(prev);
            }
        } else {
            head = null;
            tail = null;
        }
        Integer id = node.getItem().hashCode();
        nodeMap.remove(id, node);
        if (nodeMap.isEmpty()) {
            nodeMap = new HashMap<>();
        }
    }

    protected Node<T> getNode(Integer id) {
        return nodeMap.get(id);
    }

    protected List<T> getTasks() {
        if (nodeMap.isEmpty()) {
            return null;
        }
        List<T> historyList = new ArrayList<>();
        if (head != null) {
            historyList.add(head.getItem());
            Node<T> next = head.getNext();
            while (next != null) {
                historyList.add(next.getItem());
                next = next.getNext();
            }
        }
        return historyList;
    }
}
