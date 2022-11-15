package org.francis.community.core.exception;

import lombok.Data;
import lombok.Getter;

/**
 * @author Franc1s
 * @date 2022/11/15
 * @apiNote
 */
public class EmailException extends RuntimeException {
    @Getter
    private final Integer code;
    @Getter
    private final String message;

    public EmailException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
