package me.squidxtv.frameui.exceptions;

public class FontLoadingException extends RuntimeException {

    public FontLoadingException(String message) {
        super(message);
    }

    public FontLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

}
