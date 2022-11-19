package org.francis.community.modules.article.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author francis
 * @since 2022-11-17
 */
@Data
public class CommentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "一级评论id")
    private Long id;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户昵称")
    private String userNickname;

    @ApiModelProperty(value = "用户头像地址")
    private String userAvatarUrl;

    @ApiModelProperty(value = "文章id")
    private Long articleId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

}
