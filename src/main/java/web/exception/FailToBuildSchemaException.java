package web.exception;

public class FailToBuildSchemaException extends RuntimeException{
    public FailToBuildSchemaException(String message) {
        super(message);
    }
}
