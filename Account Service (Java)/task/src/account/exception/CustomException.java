package account.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class CustomException extends RuntimeException {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;


    public CustomException(HttpStatus status, String error, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = error;
        this.message = message;

    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }


}


