package org.francis.community.modules.login.controller.oauth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.francis.community.modules.login.model.dto.AccessTokenDTO;
import org.francis.community.modules.login.model.dto.GitHubUserDTO;
import org.francis.community.modules.login.service.GitHubProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Franc1s
 * @date 2022/11/14
 * @apiNote
 */
@Api(tags = "oauth登录模块")
@Slf4j
@RestController
@RequestMapping("/callback")
@RequiredArgsConstructor
public class OauthAuthorizeController {

    @Value("${github.client.id}")
    private String gitHubClientId;

    @Value("${github.client.secret}")
    private String gitHubClientSecret;

    @Value("${github.redirect.uri}")
    private String gitHubRedirectUri;

    private final GitHubProvider gitHubProvider;

    @GetMapping("/github")
    @ApiOperation(value = "github回调")
    public String githubCallback(@RequestParam String code) {
        log.info("github回调,code:{}",code);
        // 组装参数
        // TODO github密钥的安全性
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClientId(gitHubClientId);
        accessTokenDTO.setClientSecret(gitHubClientSecret);
        accessTokenDTO.setRedirectUri(gitHubRedirectUri);
        accessTokenDTO.setCode(code);

        // 根据参数获取access_token
        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        log.info("github回调,access_token:{}",accessToken);

        // 根据access_token获取github用户信息
        GitHubUserDTO gitHubUserDTO = gitHubProvider.getUserInfoByAccessToken(accessToken);
        log.info("github回调,userInfo:{}",gitHubUserDTO);

        // TODO 将用户信息记录到数据库中

        return "index.html";
    }
}
