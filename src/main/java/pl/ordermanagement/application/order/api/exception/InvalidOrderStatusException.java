package pl.ordermanagement.application.order.api.exception;

public class InvalidOrderStatusException extends IllegalStateException {
    public InvalidOrderStatusException(String message){
        super(message);
    }
}
