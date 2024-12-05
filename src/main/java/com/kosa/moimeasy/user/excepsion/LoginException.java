package com.kosa.moimeasy.user.excepsion;

public class LoginException extends RuntimeException {
    public LoginException(String message)
    {
        super(message);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
