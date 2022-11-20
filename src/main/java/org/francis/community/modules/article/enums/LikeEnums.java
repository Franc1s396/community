package org.francis.community.modules.article.enums;

/**
 * @author Franc1s
 * @date 2022/11/20
 * @apiNote
 */
public enum LikeEnums {
    /**
     * 点赞
     */
    LIKE(1000),
    /**
     * 取消点赞
     */
    CANCEL_LIKE(1001);
    private final Integer code;

    public Integer getCode() {
        return code;
    }

    LikeEnums(Integer code) {
        this.code = code;
    }
}
