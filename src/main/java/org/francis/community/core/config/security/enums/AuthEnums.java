package org.francis.community.core.config.security.enums;

/**
 * @author Franc1s
 * @date 2022/11/14
 * @apiNote
 */
public enum AuthEnums {
    /**
     * 用户密码登录
     */
    PASS("/auth/pass", "用户密码登录"),
    /**
     * 邮箱验证码登录
     */
    EMAIL("/auth/email", "邮箱验证码登录");
    private final String url;
    private final String name;

    AuthEnums(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }
}
