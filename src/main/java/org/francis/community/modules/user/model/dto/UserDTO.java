package org.francis.community.modules.user.model.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Franc1s
 * @date 2022/11/14
 * @apiNote
 */
@Data
public class UserDTO {
    @ApiModelProperty(value = "用户编号")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户手机号")
    private String phone;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像地址")
    private String avatarUrl;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "个性签名")
    private String remark;

    @ApiModelProperty(value = "用户粉丝数量")
    private Integer fans;

    @ApiModelProperty(value = "用户关注数量")
    private Integer concern;

    @ApiModelProperty(value = "第三方用户id")
    private String accountId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModified;
}
