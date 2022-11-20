package org.francis.community.modules.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("avatar_url")
    private String avatarUrl;
}
