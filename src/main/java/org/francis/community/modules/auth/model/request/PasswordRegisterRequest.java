package org.francis.community.modules.auth.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Franc1s
 * @date 2022/11/20
 * @apiNote
 */
@Data
@ApiModel("用户名密码注册参数")
public class PasswordRegisterRequest {
    @ApiModelProperty("用户名")
    @Pattern(regexp = "^\\w+$", message = "用户名只能由数字、26个英文字母或者下划线组成")
    private String username;

    @ApiModelProperty("密码")
    @Pattern(regexp = "^[a-zA-Z]\\w{5,17}$", message = "以字母开头，长度在6~18之间，只能包含字符、数字和下划线")
    private String password;

    @ApiModelProperty("用户昵称")
    @NotBlank(message = "用户昵称不能为空")
    private String nickname;
}