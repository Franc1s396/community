package org.francis.community.modules.article.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Franc1s
 * @date 2022/11/19
 * @apiNote
 */
@Data
@ApiModel("评论查询参数")
public class CommentQueryRequest {
    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("帖子id")
    private Long articleId;
}
