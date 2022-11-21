package org.francis.community.core.enums;

/**
 * @author Franc1s
 * @date 2022/11/14
 * @apiNote
 */
public enum CodeEnums {
    /**
     * 用户不存在
     */
    USER_NOT_FOUND(10000, "用户不存在~"),
    EMAIL_ERROR(10001, "邮箱验证码发送失败，请联系管理员"),
    PARAM_ERROR(10002, "参数有误,请检查"),
    ARTICLE_NOT_FOUND(10003, "帖子不存在了，换一篇看看吧~"),
    COMMENT_NOT_FOUND(10004, "评论消失了~"),
    TAG_NOT_FOUND(10005, "标签不存在了~"),
    DELETE_ERROR(10006, "您无权删除这条数据~"),
    USERNAME_TAKEN(10007, "用户名已被占用~"),
    ATTENTION_ERROR(10008,"关注出现异常，请重新刷新页面~")
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
