package org.francis.community.modules.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.francis.community.core.enums.AuthErrorEnums;
import org.francis.community.core.exception.security.OauthLoginException;
import org.francis.community.modules.auth.constant.GitHubConstants;
import org.francis.community.modules.auth.dto.GitHubAccessTokenDTO;
import org.francis.community.modules.auth.dto.GitHubUserDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Franc1s
 * @date 2022/11/14
 * @apiNote
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GitHubProvider {
    private final RestTemplate restTemplate;

    /**
     * 获取access_token
     *
     * @param gitHubAccessTokenDTO 参数
     * @return accessToken
     */
    public String getAccessToken(GitHubAccessTokenDTO gitHubAccessTokenDTO) {
        Map<String, String> map = new HashMap<>(16);
        map.put(GitHubConstants.CLIENT_ID, gitHubAccessTokenDTO.getClientId());
        map.put(GitHubConstants.CLIENT_SECRET, gitHubAccessTokenDTO.getClientSecret());
        map.put(GitHubConstants.STATE, gitHubAccessTokenDTO.getState());
        map.put(GitHubConstants.CODE, gitHubAccessTokenDTO.getCode());
        map.put(GitHubConstants.REDIRECT_URI, gitHubAccessTokenDTO.getRedirectUri());
        Map<String, String> accessTokenMap =
                restTemplate.postForObject("https://github.com/login/oauth/access_token", map, Map.class);

        if (CollectionUtils.isEmpty(accessTokenMap)) {
            throw new OauthLoginException(AuthErrorEnums.OAUTH_LOGIN_ERROR.getCode(), AuthErrorEnums.OAUTH_LOGIN_ERROR.getName());
        }
        String accessToken = accessTokenMap.get(GitHubConstants.ACCESS_TOKEN);
        if (!StringUtils.hasText(accessToken)) {
            throw new OauthLoginException(AuthErrorEnums.OAUTH_LOGIN_ERROR.getCode(), AuthErrorEnums.OAUTH_LOGIN_ERROR.getName());
        }
        return accessToken;
    }

    /**
     * 通过access_token从github获取用户信息
     *
     * @param accessToken accessToken
     * @return 用户信息
     */
    public GitHubUserDTO getUserInfoByAccessToken(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<GitHubUserDTO> exchange =
                restTemplate.exchange("https://api.github.com/user", HttpMethod.GET, httpEntity, GitHubUserDTO.class);
        return exchange.getBody();
    }
}
