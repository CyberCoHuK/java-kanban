package manager;

public class Node<T> {
    private final T data;

    private Node<T> next;
    private Node<T> prev;

    Node(Node<T> prev, T data, Node<T> next) {
        if (prev != null && data.equals(prev.data)) {
            this.prev = prev.getPrev();
            this.next = prev.getNext();
        } else {
            this.next = next;
            this.prev = prev;
        }
        this.data = data;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public Node<T> getPrev() {
        return prev;
    }

    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }

    public T getItem() {
        return this.data;
    }
}
