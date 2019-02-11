package co.nilin.springamqp.exception.exceptions;

import java.io.Serializable;

public class BusinessException extends Exception implements Serializable {

    private int status;
    private String message;
    private String error;

    public BusinessException(int status, String error, String message,  Throwable cause) {
        super(message, cause);
        this.status = status;
        this.message=message;
        this.error= error;

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
