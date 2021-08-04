package service;

public class ExamenException extends RuntimeException{
    public ExamenException() {
    }

    public ExamenException(String message) {
        super(message);
    }

    public ExamenException(String message, Throwable cause) {
        super(message, cause);
    }
}
