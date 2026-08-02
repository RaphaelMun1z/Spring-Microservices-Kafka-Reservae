package user_profile_service.exceptions;

import java.util.List;

public class ExceptionResponse {
    private String localDateTime;
    private List<String> message;
    private String details;

    public ExceptionResponse(String localDateTime, List<String> message, String details) {
        this.localDateTime = localDateTime;
        this.message = message;
        this.details = details;
    }

    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}