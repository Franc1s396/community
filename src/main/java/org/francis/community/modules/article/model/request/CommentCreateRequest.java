package org.francis.community.modules.article.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Franc1s
 * @date 2022/11/19
 * @apiNote
 */
@Data
@ApiModel("发布评论参数")
public class CommentCreateRequest {
    @ApiModelProperty(value = "评论内容")
    @NotBlank(message = "评论内容不能为空")
    private String content;

    @ApiModelProperty(value = "文章id")
    @NotNull(message = "文章id不能为空")
    private Long articleId;
}
