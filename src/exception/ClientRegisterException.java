package exception;

public class ClientRegisterException extends RuntimeException {
    public ClientRegisterException(final String message) {
        super(message);
    }
}