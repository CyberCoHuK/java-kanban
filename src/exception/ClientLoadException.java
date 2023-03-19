package exception;

public class ClientLoadException extends RuntimeException {
    public ClientLoadException(final String message) {
        super(message);
    }
}
