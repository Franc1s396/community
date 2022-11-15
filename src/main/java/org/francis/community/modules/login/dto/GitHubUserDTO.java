package org.francis.community.modules.login.dto;

import lombok.Data;

/**
 * @author Franc1s
 * @date 2022/11/14
 * @apiNote
 */
@Data
public class GitHubUserDTO {
    private Long id;
    private String login;
    private String avatarUrl;
}
