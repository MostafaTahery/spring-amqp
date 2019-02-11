package co.nilin.springamqp.data.dto;

public class ErrorResponse {
    private String message;
    private String error;
    private int status;

    public ErrorResponse(int status, String error, String message) {
        this.message = message;
        this.error = error;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public int getStatus() {
        return status;
    }
}
