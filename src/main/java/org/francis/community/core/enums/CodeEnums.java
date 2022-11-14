package org.francis.community.core.enums;

/**
 * @author Franc1s
 * @date 2022/11/14
 * @apiNote
 */
public enum CodeEnums {
    /**
     * 测试
     */
    TEST(10000,"TEST")
    ;
    private final Integer code;
    private final String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    CodeEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
