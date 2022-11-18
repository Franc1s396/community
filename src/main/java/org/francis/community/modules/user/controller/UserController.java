package org.francis.community.modules.user.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.francis.community.core.model.AjaxResult;
import org.francis.community.core.utils.SecurityUtils;
import org.francis.community.modules.user.model.dto.UserDTO;
import org.francis.community.modules.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author francis
 * @since 2022-11-14
 */
@Api(tags = "用户模块")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login-info")
    @ApiOperation(value = "登录用户个人信息")
    public AjaxResult findLoginUserInfo(){
        Long userId = SecurityUtils.getUserId();
        UserDTO userInfo=userService.findUserById(userId);
        return AjaxResult.success(userInfo);
    }
}

