package exception;

public class ClientSaveException extends RuntimeException {
    public ClientSaveException(final String message) {
        super(message);
    }
}