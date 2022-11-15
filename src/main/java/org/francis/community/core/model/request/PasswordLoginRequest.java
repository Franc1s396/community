package org.francis.community.core.model.request;

import lombok.Data;

/**
 * @author Franc1s
 * @date 2022/11/15
 * @apiNote
 */
@Data
public class PasswordLoginRequest {
    private String username;

    private String password;
}
