package org.francis.community.modules.article.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

/**
 * @author Franc1s
 * @date 2022/11/20
 * @apiNote
 */
@Data
@ApiModel("帖子点赞参数")
public class ArticleLikeRequest {
    @ApiModelProperty("帖子id")
    @NotNull(message = "帖子id不能为空")
    private Long articleId;
    @ApiModelProperty("是否点赞(true: 点赞 false: 取消点赞)")
    @NotNull(message = "选择是否点赞")
    private Boolean isLike;
}
