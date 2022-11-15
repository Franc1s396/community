package org.francis.community.core.model.request;

import lombok.Data;

/**
 * @author Franc1s
 * @date 2022/11/15
 * @apiNote
 */
@Data
public class EmailLoginRequest {
    /**
     * 邮箱
     */
    private String email;
    /**
     * 验证码
     */
    private String code;
}
