package org.francis.community.modules.article.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Franc1s
 * @date 2022/11/17
 * @apiNote
 */
@Data
@ApiModel("创建文章参数")
public class CreateArticleRequest {
    @ApiModelProperty(value = "帖子标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty(value = "帖子内容")
    @NotBlank(message = "内容不能为空")
    private String content;

    @ApiModelProperty(value = "标签编号")
    @NotNull(message = "标签不能为空")
    private Long tagId;
}
