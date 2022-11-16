package org.francis.community.modules.auth.dto;

import lombok.Data;

/**
 * @author Franc1s
 * @date 2022/11/14
 * @apiNote
 */
@Data
public class GitHubAccessTokenDTO {
    private String clientId;
    private String clientSecret;
    private String state;
    private String code;
    private String redirectUri;
}
