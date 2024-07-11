package me.squidxtv.frameui.api.exceptions;

/**
 * General exception for all errors related to Frames Api
 */
public class FramesApiException extends RuntimeException {
    public FramesApiException() {
    }

    public FramesApiException(String message) {
        super(message);
    }

    public FramesApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public FramesApiException(Throwable cause) {
        super(cause);
    }

    public FramesApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
