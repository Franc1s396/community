package org.francis.community.core.enums;

/**
 * @author Franc1s
 * @date 2022/11/15
 * @apiNote
 */
public enum AuthErrorEnums {
    /**
     * 认证
     */
    forceLoginOut(70000001, "您的账号在其他设备登录"),
    hasUserOnline(70000002, "当前已有用户在登录"),
    tokenInvalid(70000003, "登录已经过期，请重新登录"),
    tokenIllegal(70000004, "无效令牌"),
    methodNotSupport(70000005, "请求的方法不支持"),
    passwordIncorrect(70000006, "用户名或者密码错误"),
    accessDenied(70000007, "没有操作该功能的权限"),
    verifyCodeIncorrect(70000008, "验证码不正确"),
    accountNotExist(70000010, "账户不存在"),
    tokenExpired(70000009, "登录已经过期，请重新登录"),
    dataInvalid(70000010, "数据格式不正确"),
    requestFail(70000011, "请求失败"),
    AccountIsLock(70000012, "账号已经被禁用"),
    AuthFailure(70000013, "认证失败"),
    DeviceNotSupport(70000014, "设备未识别"),
    OAUTH_LOGIN_ERROR(70000015, "授权登录失败");

    private final Integer code;
    private final String name;

    AuthErrorEnums(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
