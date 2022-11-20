package org.francis.community.modules.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.francis.community.core.config.security.authentication.LoginSuccessResponse;
import org.francis.community.core.exception.ServiceException;
import org.francis.community.core.model.AjaxResult;
import org.francis.community.core.utils.JwtUtils;
import org.francis.community.modules.auth.model.dto.GitHubAccessTokenDTO;
import org.francis.community.modules.auth.model.dto.GitHubUserDTO;
import org.francis.community.modules.auth.service.GitHubProvider;
import org.francis.community.modules.user.model.dto.UserDTO;
import org.francis.community.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Franc1s
 * @date 2022/11/14
 * @apiNote
 */
@Api(tags = "oauth登录模块")
@Slf4j
@RestController
@RequiredArgsConstructor
public class OauthAuthorizeController {

    @Value("${github.client.id}")
    private String gitHubClientId;

    @Value("${github.client.secret}")
    private String gitHubClientSecret;

    @Value("${github.redirect.uri}")
    private String gitHubRedirectUri;

    private final GitHubProvider gitHubProvider;

    private final UserService userService;

    @GetMapping("/callback/github")
    @ApiOperation(value = "github回调")
    public AjaxResult githubCallback(@RequestParam String code) {
        log.info("github回调,code:{}", code);
        // 组装参数
        GitHubAccessTokenDTO gitHubAccessTokenDTO = new GitHubAccessTokenDTO();
        gitHubAccessTokenDTO.setClientId(gitHubClientId);
        gitHubAccessTokenDTO.setClientSecret(gitHubClientSecret);
        gitHubAccessTokenDTO.setRedirectUri(gitHubRedirectUri);
        gitHubAccessTokenDTO.setCode(code);

        // 根据参数获取access_token
        String accessToken = gitHubProvider.getAccessToken(gitHubAccessTokenDTO);

        // 根据access_token获取github用户信息
        GitHubUserDTO gitHubUserDTO = gitHubProvider.getUserInfoByAccessToken(accessToken);

        // 将用户信息记录到数据库中
        UserDTO user= null;
        try {
            user = userService.findOauthUserByAccountId(gitHubUserDTO.getId());
        } catch (ServiceException e) {
            UserDTO userDTO = new UserDTO();
            userDTO.setAccountId(gitHubUserDTO.getId().toString());
            userDTO.setNickname(gitHubUserDTO.getLogin());
            // 防止重复的username
            userDTO.setUsername("github_"+gitHubUserDTO.getId());
            userDTO.setAvatarUrl(gitHubUserDTO.getAvatarUrl());
            user=userService.saveOauthUser(userDTO);
        }

        //转换成JWT token并返回
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username",user.getUsername());
        String token = JwtUtils.createToken(claims);

        LoginSuccessResponse response = new LoginSuccessResponse();
        response.setUsername(user.getUsername());
        response.setToken(token);

        log.info("github授权登录用户 id:{},username:{}",user.getId(),user.getUsername());
        return AjaxResult.success("登陆成功", response);
    }
}
