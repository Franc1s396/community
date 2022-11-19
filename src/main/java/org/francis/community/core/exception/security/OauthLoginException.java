package org.francis.community.core.exception.security;

/**
 * @author Franc1s
 * @date 2022/11/14
 * @apiNote
 */
public class OauthLoginException extends RuntimeException{
    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    public OauthLoginException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
