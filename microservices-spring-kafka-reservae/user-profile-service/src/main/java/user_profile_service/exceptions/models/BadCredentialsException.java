package user_profile_service.exceptions.models;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException(String msg) {
        super(msg);
    }
}