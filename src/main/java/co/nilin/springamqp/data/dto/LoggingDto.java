package co.nilin.springamqp.data.dto;



import java.io.Serializable;

public class LoggingDto implements Serializable {
    private String status="";
    private String error="";
    private String message="";

    public LoggingDto() {
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public void setMessage(String message) {
        this.message = message+"...";
    }

    public void setError(String error) {
        this.error = error+"...";
    }

    public void setStatus(String status) {
        this.status = status+"...";
    }
}
